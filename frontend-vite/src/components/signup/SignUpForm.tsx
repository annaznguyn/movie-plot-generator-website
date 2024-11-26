import { Link, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import axios from 'axios';
import { supabase } from '../../client/SupabaseClient';

const baseURL: string = import.meta.env.VITE_BASE_URL;

const SignUpForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [email, setEmail] = useState('');
    const [role, setRole] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event: any) => {
        event.preventDefault();
        setRole("admin");

        const { data, error } = await supabase.auth.signUp({
            email,
            password,
        });
        if (error) {
            setErrorMessage(error.message);
            return { user: null, error: error };
        }

        let userId = data.user?.id;

        await axios.post(baseURL + "/api/auth/signup", { 
            userId,
            username, 
            email, 
            password, 
            confirmPassword, 
            role
        })
        .then(response => {
            console.log(response.data);
            setSuccessMessage('User registered successfully');
            setErrorMessage('');
            navigate("/dashboard");
        })
        .catch(error => {
            console.error(error);
            setErrorMessage('Error');
            setSuccessMessage('');
        })
        
    };

    return (
        <div
            className="hero min-h-screen"
            style={{
                backgroundImage: "url(https://cdn.myportfolio.com/a7dcc6d5ac1134b2d40ac6d1c5552304/1f0a0456-b934-4f28-beee-c47de7308667_rw_1920.gif?h=5ecc8b473e70030b5e3d8b8ef826ca1c)"
            }}>
            <div className="hero-overlay bg-opacity-60" />
            <div className="hero-content flex-col lg:flex-row">
                
                <div className="flex justify-center self-center z-10 w-full">
                    <div className="p-12 bg-white mx-auto rounded-2xl">
                        <div className="mb-4">
                            <h3 className="font-semibold text-2xl text-slate-700 cursor-default">Sign Up</h3>
                            <p className="text-gray-500">Please sign up to your account.</p>
                        </div>
                        <form onSubmit={handleSubmit} className="space-y-5">
                            <div className="form-control">
                                <label className="label">
                                    <span className="label-text font-medium text-gray-700">Username</span>
                                </label>
                                <input 
                                    type="text" 
                                    placeholder="Username" 
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    className="input input-bordered border-gray-300 bg-white" required 
                                />
                            </div>
                            <div className="form-control">
                                <label className="label">
                                    <span className="label-text font-medium text-gray-700">Email</span>
                                </label>
                                <input 
                                    type="email" 
                                    placeholder="Email address" 
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    className="input input-bordered border-gray-300 bg-white" required 
                                />
                            </div>
                            <div className="flex items-center justify-between space-x-5">
                                <div className="form-control">
                                    <label className="label">
                                        <span className="label-text font-medium text-gray-700">Password</span>
                                    </label>
                                    <input 
                                        type="password" 
                                        placeholder="Password" 
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        className="input input-bordered border-gray-300 bg-white" required 
                                    />
                                </div>
                                <div className="form-control">
                                    <label className="label">
                                        <span className="label-text font-medium text-gray-700">Comfirm password</span>
                                    </label>
                                    <input
                                        type="password" 
                                        placeholder="Password" 
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        className="input input-bordered border-gray-300 bg-white" required 
                                        />
                                </div>
                            </div>
                            {/* <div className="flex items-center justify-between">
                                <div className="flex items-center">
                                    <input type="checkbox" className="h-4 w-4 bg-white accent-blue-700 border-gray-300 rounded"/>
                                    <label className="label">
                                        <span className="label-text text-sm text-gray-700 font-semibold">Agree to Terms and Conditions</span>
                                    </label>
                                </div>
                            </div> */}
                            <div className="form-control">
                                <button type="submit" className="btn btn-primary bg-blue-600 mt-2 text-white hover:bg-blue-700 outline-none">Sign up</button>
                            </div>
                            <div className="flex items-center justify-between">
                                <p className="text-xs ml-8">Have an account?</p>
                                <Link to="/signin" className="text-xs text-blue-600 hover:text-blue-700 mr-10">Sign in now</Link>
                            </div>
                            {successMessage && <p className="text-center mt-4 text-green-500">{successMessage}</p>}
                            {errorMessage && <p className="text-center mt-4 text-red-600">{errorMessage}</p>}
                        </form>
                        <div className="mt-10">
                            <p className="text-center text-xs text-gray-500">&copy; Story Tree 2024</p>
                        </div>
                    </div>
                </div>
            </div>
	    </div>
    )
}

export default SignUpForm;

