import { Link } from "react-router-dom";

const StoryHeader = () => {
    return (
        <header className="stick top-0 z-50 py-2">
            <div className="container min-w-full">
                <div className="navbar px-0">
                    <div className="navbar-start">
                        <Link to="#" className="font-semibold">Dashboard</Link>
                    </div>
                    <div className="navbar-end">
                        <div className="dropdown dropdown-end">
                            <div tabIndex={0} role="button" className="btn btn-ghost btn-circle avatar">
                                <div className="rounded-full">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="size-6">
                                        <path strokeLinecap="round" strokeLinejoin="round" d="M17.982 18.725A7.488 7.488 0 0 0 12 15.75a7.488 7.488 0 0 0-5.982 2.975m11.963 0a9 9 0 1 0-11.963 0m11.963 0A8.966 8.966 0 0 1 12 21a8.966 8.966 0 0 1-5.982-2.275M15 9.75a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
                                    </svg>
                                </div>
                            </div>
                            <ul
                                tabIndex={0}
                                className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow">
                                <li><a>Account</a></li>
                                <li><a>Management</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        
        
    );
}

export default StoryHeader;