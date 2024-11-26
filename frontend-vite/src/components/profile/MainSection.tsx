import React, { useState, useEffect } from "react";
import { supabase } from "../../client/SupabaseClient";
import axios from "axios";
import stormtrooper from "./stormtrooper.png";

const baseURL: string = import.meta.env.VITE_BASE_URL;

interface UserProfile {
    email: string;
    username: string | null;
}

interface UserStats {
    totalStories: number;
    lastStoryCreated: string;
    topGenre: string;
}

interface PasswordChangeError {
    message: string;
    field?: string;
}

const MainSection: React.FC = () => {
    const [profile, setProfile] = useState<UserProfile | null>(null);
    const [stats, setStats] = useState<UserStats | null>(null);
    const [loading, setLoading] = useState(true);

    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [passwordError, setPasswordError] = useState<PasswordChangeError | null>(null);
    const [passwordSuccess, setPasswordSuccess] = useState(false);

    const getSessionUser = async () => {
        const { data, error } = await supabase.auth.getSession();

        if (error) {
            console.error(error.message);
        }

        console.log(data.session);

        const session = data.session;

        if (session) {
            const user = session.user;
            return user;
        }

    }

    const calculateUserStats = async () => {
        try {
            const user = await getSessionUser();
            const email = user?.email;

            const response = await axios.get(baseURL + "/stories", {
                params: { email: email }
            });

            const stories = response.data;

            const totalStories = stories.length;

            const lastStoryDate = stories.length > 0
                ? stories.reduce((latest: Date, story: any) => {
                    const storyDate = new Date(story.creationDate);
                    return storyDate > latest ? storyDate : latest;
                }, new Date(stories[0].creationDate))
                : null;

            const lastStoryCreated = lastStoryDate
                ? lastStoryDate.toLocaleDateString('en-US', {
                    year: 'numeric',
                    month: 'short',
                    day: 'numeric'
                })
                : "No stories yet";

            const genreCounts = stories.reduce((acc: { [key: string]: number }, story: any) => {
                const genre = story.genre || 'Uncategorized';
                acc[genre] = (acc[genre] || 0) + 1;
                return acc;
            }, {});

            const topGenre = Object.entries(genreCounts).length > 0
                ? Object.entries(genreCounts)
                    .sort((a, b) => (b[1] as number) - (a[1] as number))[0][0]
                : "N/A";

            setStats({
                totalStories,
                lastStoryCreated,
                topGenre
            });

        } catch (error) {
            console.error("Error calculating user stats:", error);
            setStats({
                totalStories: 0,
                lastStoryCreated: "No stories yet",
                topGenre: "N/A"
            });
        }
    };

    const fetchProfile = async () => {
        try {
            const { data: { session }, error } = await supabase.auth.getSession();

            if (error) throw error;

            if (session?.user) {
                const user = session.user;
                setProfile({
                    email: user.email!,
                    username: user.user_metadata?.username || null,
                });
                setEmail(user.email!);
                setUsername(user.user_metadata?.username || "");
            }
        } catch (error) {
            console.error("Error fetching profile:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchProfile();
        calculateUserStats();
    }, []);

    const handleUpdateProfile = async (e: React.FormEvent) => {
        e.preventDefault();
        setPasswordError(null);
        setPasswordSuccess(false);

        try {
            if (email !== profile?.email) {
                const { error } = await supabase.auth.updateUser({ email });
                if (error) throw error;
            }

            if (username !== profile?.username) {
                const { error } = await supabase.auth.updateUser({
                    data: { username }
                });
                if (error) throw error;
            }

            if (password && confirmPassword) {
                if (password !== confirmPassword) {
                    setPasswordError({ message: "Passwords do not match" });
                    return;
                }

                try {
                    const response = await axios.put(
                        `localhost:8080/users/${profile?.username}`,
                        { password },
                        {
                            params: { method: "password" },
                            headers: {
                                "Content-Type": "application/json",
                                "Authorization": `Bearer ${(await supabase.auth.getSession()).data.session?.access_token}`
                            }
                        }
                    );

                    if (response.status === 200) {
                        setPasswordSuccess(true);

                        setPassword("");
                        setConfirmPassword("");
                    }
                } catch (error: any) {
                    setPasswordError({
                        message: error.response?.data?.message || "Failed to update password"
                    });
                    return;
                }
            }

            fetchProfile();
        } catch (error: any) {
            console.error("Error updating profile:", error);
            setPasswordError({
                message: error.message || "An error occurred while updating profile"
            });
        }
    };

    if (loading) {
        return <div className="flex justify-center items-center h-screen">
            <span className="loading loading-spinner loading-lg"></span>
        </div>;
    }

    return (
        <div className="container mx-auto px-4 py-6 max-w-2xl pb-16">
            <div className="flex flex-col items-center space-y-6">
                <div className="bg-base-200 rounded-3xl p-6 w-full">
                    <div className="flex flex-col items-center gap-6">
                        <div className="avatar online">
                            <div className="w-24 rounded-full">
                                <img
                                    src={stormtrooper}
                                    alt="stormtrooper"
                                    className="object-cover object-center scale-90"
                                />
                            </div>
                        </div>

                        <div className="text-lg font-bold">
                            {profile?.username || profile?.email}
                        </div>


                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 w-full">
                            <div className="stats shadow">
                                <div className="stat text-center">
                                    <div className="stat-title text-xs">Total stories</div>
                                    <div className="stat-value text-lg">
                                        {stats?.totalStories || 0}
                                    </div>
                                </div>
                            </div>

                            <div className="stats shadow">
                                <div className="stat text-center">
                                    <div className="stat-title text-xs">Last story created</div>
                                    <div className="stat-value text-sm">
                                        {stats?.lastStoryCreated}
                                    </div>
                                </div>
                            </div>

                            <div className="stats shadow">
                                <div className="stat text-center">
                                    <div className="stat-title text-xs">Top genre</div>
                                    <div className="stat-value text-sm">
                                        {stats?.topGenre}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="bg-base-200 rounded-3xl p-6 w-full">
                    <h2 className="text-lg font-bold mb-4 text-center">Update Profile</h2>
                    <form onSubmit={handleUpdateProfile} className="flex flex-col items-center space-y-4">

                        <div className="form-control w-full max-w-xs">
                            <label className="label justify-center">
                                <span className="label-text text-sm">Email</span>
                            </label>
                            <input
                                type="email"
                                className="input input-bordered input-md w-full rounded-3xl"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>

                        <div className="form-control w-full max-w-xs">
                            <label className="label justify-center">
                                <span className="label-text text-sm">Username</span>
                            </label>
                            <input
                                type="text"
                                className="input input-bordered input-md w-full rounded-3xl"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                            />
                        </div>

                        <div className="form-control w-full max-w-xs">
                            <label className="label justify-center">
                                <span className="label-text text-sm">New Password</span>
                            </label>
                            <input
                                type="password"
                                className={`input input-bordered input-md w-full rounded-3xl ${passwordError?.field === "password" ? "input-error" : ""
                                    }`}
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder="Leave blank to keep current"
                            />
                        </div>

                        <div className="form-control w-full max-w-xs pb-6">
                            <label className="label justify-center">
                                <span className="label-text text-sm">Confirm Password</span>
                            </label>
                            <input
                                type="password"
                                className={`input input-bordered input-md w-full rounded-3xl ${passwordError?.field === "confirmPassword" ? "input-error" : ""
                                    }`}
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                placeholder="Leave blank to keep current"
                            />
                        </div>

                        {passwordError && (
                            <div className="alert alert-error">
                                <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                                <span>{passwordError.message}</span>
                            </div>
                        )}

                        {passwordSuccess && (
                            <div className="alert alert-success">
                                <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                                <span>Password updated successfully</span>
                            </div>
                        )}

                        <button
                            type="submit"
                            className="btn btn-md btn-neutral rounded-3xl"
                        >
                            Save Changes
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default MainSection;