import { Link, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { supabase } from '../../client/SupabaseClient';

const SignInForm = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const checkAuth = async () => {
            const { data: { session } } = await supabase.auth.getSession();
            if (session) {
                navigate('/dashboard');
            }
        };
        checkAuth();
    }, [navigate]);

    const handleSubmit = async (event: any) => {
        event.preventDefault();
        setLoading(true);
    
        try {
            const { data, error } = await supabase.auth.signInWithPassword({
                email: email,
                password: password,
            });
    
            if (error) {
                console.log(error.message);
                return;
            }
    
            if (data.user) {
                console.log("Successful login:", data.user);
                navigate("/dashboard", { replace: true });
            }
    
        } catch (error) {
            console.error("Sign in error:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div
            className="hero min-h-screen"
            style={{
                backgroundImage: "url(https://cdn.myportfolio.com/a7dcc6d5ac1134b2d40ac6d1c5552304/1f0a0456-b934-4f28-beee-c47de7308667_rw_1920.gif?h=5ecc8b473e70030b5e3d8b8ef826ca1c)"
            }}>
            <div className="hero-overlay bg-opacity-60" />
            <div className="hero-content flex-col lg:flex-row">
                <div className="text-center lg:text-left">
                    <h1 className="text-5xl font-bold text-white">Explore the power of storytelling</h1>
                    <p className="py-6 text-white">
                        Take a dive into intuitive story building and narrative creation with our tree-based structure.
                    </p>
                </div>
                <div className="flex justify-center self-center z-10 w-full">
                    <form onSubmit={handleSubmit} className="p-12 bg-white mx-auto w-3/4 rounded-2xl">
                        <div className="mb-4">
                            <h3 className="font-semibold text-2xl text-slate-700 cursor-default">Sign In</h3>
                            <p className="text-gray-500">Please sign in to your account.</p>
                        </div>
                        <div className="space-y-5">
                            <div className="form-control">
                                <label className="label">
                                    <span className="label-text font-medium text-gray-700">Email</span>
                                </label>
                                <input 
                                    type="email" 
                                    placeholder="Email address"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    className="input text-black input-bordered border-gray-300 bg-white" required />
                            </div>
                            <div className="form-control">
                                <label className="label">
                                    <span className="label-text font-medium text-gray-700">Password</span>
                                </label>
                                <input 
                                    type="password" 
                                    placeholder="Password" 
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    className="input text-black input-bordered border-gray-300 bg-white" required />
                            </div>
                            {/* <div className="flex items-center justify-between">
                                <div className="flex items-center">
                                    <input type="checkbox" className="h-4 w-4 bg-white accent-blue-700 border-gray-300 rounded"/>
                                    <label className="label">
                                        <span className="label-text text-sm text-gray-700 font-semibold">Remember me</span>
                                    </label>
                                </div>
                                <div>
                                    <a href="#" className="text-sm text-blue-600 hover:text-blue-700 ml-10">
                                        Forgot your password?
                                    </a>
                                </div>
                            </div> */}
                            <div className="form-control mt-6">
                                <button className="btn bg-blue-600 mt-1 btn-primary text-white hover:bg-blue-700 outline-none">{loading ? 'Signing In...' : 'Sign In'}</button>
                            </div>
                            <div className="flex items-center justify-between">
                                <p className="text-xs ml-8">Don't have an account?</p>
                                <Link to="/signup" className="text-xs text-blue-600 hover:text-blue-700 mr-10">Sign up now</Link>
                            </div>
                        </div>
                        <div className="mt-10">
                            <p className="text-center text-xs text-gray-500">&copy; Story Tree 2024</p>
                        </div>
                    </form>
                </div>
            </div>
	    </div>
    )
}

export default SignInForm;

