import React, { ChangeEvent, MouseEvent, useState, useEffect } from "react";
import DashboardHeader from "./DashboardHeader";
import Sidebar from "./Sidebar";
import DashboardCard from "./DashboardCard";
import { supabase } from "../../client/SupabaseClient";
import axios from "axios";

const baseURL: string = import.meta.env.VITE_BASE_URL;

export interface DashboardCardInterface {
    id: number;
    title: string;
    image: string;
    summary: string;
    created: Date;
    modified: Date;
    genre: string;
}

interface MainSectionProps {
    setPropStories: React.Dispatch<React.SetStateAction<DashboardCardInterface[]>>;
    dateFilterOption: string;
    genreFilterOption: string;
    searchQuery: string;
}

export interface ImageApiResponse {
    id: string;
    author: string;
    width: number;
    height: number;
    url: string;
    download_url: string;
}

const MainSection: React.FC<MainSectionProps> = ({ setPropStories, dateFilterOption, genreFilterOption, searchQuery }) => {
    const [sortOption, setSortOption] = useState<string>("");
    const [randomNumber, setRandomNumber] = useState<number>(10);
    const [stories, setStories] = useState<DashboardCardInterface[]>([]);

    const generateRandomSeed = () => {
        setRandomNumber(Math.floor(Math.random() * 255));
    };

    const [copies, setCopies] = useState(stories);

    type DateFilterOption = "Today" | "Last week" | "Last month" | "Last year" | "Any";

    const addStory = (title: string, summary: string, genre: string) => {
        const newStory: DashboardCardInterface = {
            id: stories.length + 1,
            title,
            summary,
            image: `https://picsum.photos/seed/${stories.length + 1}/350/300`,
            created: new Date(),
            modified: new Date(),
            genre
        };

        setStories(prevStories => [...prevStories, newStory]);
        setCopies(stories);
    };

    useEffect(() => {
        setStories(stories.map((story, index) => ({
            ...story,
            image: `https://picsum.photos/seed/${index + 1}/350/300`
        })));
    }, []);

    const handleSortOptionChange = (option: string) => {
        setSortOption(option);

        let sortedStories;

        if (option === "Title") {
            sortedStories = [...copies].sort((a, b) => {
                return a.title.localeCompare(b.title);
            });
        } else if (option === "Genre") {
            sortedStories = [...copies].sort((a, b) => {
                return a.genre.localeCompare(b.genre);
            });
        } else if (option === "Created") {
            sortedStories = [...copies].sort((a, b) => {
                return new Date(b.created).getTime() - new Date(a.created).getTime();
            });
        } else if (option === "Most recent") {
            sortedStories = [...copies].sort((a, b) => {
                return new Date(b.modified).getTime() - new Date(a.modified).getTime();
            });
        }

        if (sortedStories) {
            setCopies([...sortedStories]);
        }
    };

    const filterStories = (timeframe: DateFilterOption, genre?: string) => {
        let filtered = [...stories];

        if (timeframe === "Any" && genre === "") {
            return filtered;
        }

        if (timeframe === "Any" && genre === "Any") {
            return filtered;
        }

        if (genre && genre !== "Any") {
            filtered = filtered.filter(story => story.genre === genre);
        }

        if (timeframe != "Any") {
            const now = new Date();
            let startDate = new Date();

            switch (timeframe) {
                case "Today":
                    startDate = new Date(now.setHours(0, 0, 0, 0));
                    break;
                case "Last week":
                    startDate = new Date(now.setDate(now.getDate() - 7));
                    break;
                case "Last month":
                    startDate = new Date(now.setMonth(now.getMonth() - 1));
                    break;
                case "Last year":
                    startDate = new Date(now.setFullYear(now.getFullYear() - 1));
                    break;
            }

            filtered = filtered.filter(story => {
                const storyDate = new Date(story.created.getTime());
                return storyDate > startDate;
            });
        }

        setCopies(filtered);
        return filtered;
    };

    const searchStories = (query: string) => {
        if (!query) {
            return stories;
        }

        const regex = new RegExp(query, 'i');

        return stories.filter(story =>
            regex.test(story.title) || regex.test(story.summary)
        );
    };

    useEffect(() => {
        setPropStories(stories);
        setCopies(stories);
    }, [stories]);

    const [dateState, setDateState] = useState<DateFilterOption>("Last year");

    const handleDateFilter = (value: string) => {
        setDateState(value as DateFilterOption);
        return value as DateFilterOption;
    };

    const handleUpdateStory = async (storyId: number, newTitle: string, newSummary: string, newGenre: string) => {
        const user = await getSessionUser();
        const email = user?.email;

        try {
            await axios.put(`${baseURL}/stories/${storyId}`, {
                storyname: newTitle,
                username: email,
                genre: newGenre,
                description: newSummary
            });


            setStories(prevStories =>
                prevStories.map(story =>
                    story.id === storyId
                        ? {
                            ...story,
                            title: newTitle,
                            summary: newSummary,
                            genre: newGenre,
                            modified: new Date()
                        }
                        : story
                )
            );

        } catch (error) {
            console.error('Error updating story:', error);
        }
    };

    const handleDeleteStory = async (storyId: number) => {
        try {
            await axios.delete(`${baseURL}/stories/${storyId}/delete`);
  
            setStories(prevStories => {
                const newStories = prevStories.filter(story => {
                    return story.id !== storyId;
                });
                return newStories;
            });
    
        } catch (error) {
            console.error('Error deleting story:', error);
        }
    };

    useEffect(() => {
        setCopies(filterStories(handleDateFilter(dateFilterOption), genreFilterOption));
    }, [genreFilterOption, dateFilterOption]);

    useEffect(() => {
        setCopies(searchStories(searchQuery));
    }, [searchQuery]);

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

    const fetchAllStories = async () => {
        const user = await getSessionUser();
        const email = user?.email;
        console.log(email);
        axios.get(baseURL + "/stories", {
            params: {
                email: email
            }
        })
            .then(response => {
                console.log(response);
                const mappedStories = response.data.map((story: any) => ({
                    id: story.storyId,
                    title: story.storyName,
                    image: `https://picsum.photos/seed/${story.storyId}/350/300`,
                    summary: story.description,
                    created: new Date(story.creationDate),
                    modified: new Date(story.lastUpdate),
                    genre: story.genre
                }));

                setStories(mappedStories);
                setCopies(mappedStories);
            })
            .catch(error => {
                console.log(error.message);
            })
    }
    
    useEffect(() => {
        fetchAllStories();
    }, []);

    return (
        <>
            <div className="container pt-12 pb-12">
                <div className="flex rounded-3xl bg-base-200">
                    <ul className="menu bg-base-300 w-56 w-1/5 p-4 rounded-l-3xl">
                        <Sidebar handleStoryCreation={addStory} 
                        refreshStories={fetchAllStories} />
                    </ul>

                    <div className="w-4/5 bg-base-200 p-4 rounded-xl">
                        <div className="flex justify-between items-center pb-14">
                            <DashboardHeader onSortOptionChange={handleSortOptionChange} />
                        </div>

                        <div className="h-[600px] overflow-y-auto">
                            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 lg:gap-8 xl:gap-10">
                                {copies.map((story) => (
                                    <DashboardCard
                                        key={story.id}
                                        id={story.id}
                                        title={story.title}
                                        image={story.image}
                                        summary={story.summary}
                                        genre={story.genre}
                                        created={story.created}
                                        modified={story.modified}
                                        onUpdate={handleUpdateStory}
                                        onDelete={handleDeleteStory}
                                    />
                                ))}
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </>
    );
};

export default MainSection;