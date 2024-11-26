import React, { useState } from "react";
import ThemeToggler from "./ThemeToggler";
import { Link } from 'react-router-dom';
import { DashboardCardInterface } from "./MainSection";
import ProfilePage from "../../pages/ProfilePage";

interface NavigationProps {
    stories: DashboardCardInterface[];
    updateFilterOptions: (genre: string, date: string) => void;
    updateSearchQuery: (query: string) => void;
};

const Header: React.FC<NavigationProps> = ({ stories, updateFilterOptions, updateSearchQuery }) => {
    const [genreFilterOption, setGenreFilterOption] = useState("");
    const [dateFilterOption, setDateFilterOption] = useState("");

    const uniqueGenres = Array.from(new Set(stories.map(item => item.genre)));

    const handleGenreFilterSelect = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setGenreFilterOption(event.target.value);
    }

    const handleDateFilterSelect = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setDateFilterOption(event.target.value);
    }

    const handleConfirmFilterOption = () => {
        updateFilterOptions(genreFilterOption, dateFilterOption);
    }

    const handleSearchQueryUpdate = (event: React.ChangeEvent<HTMLInputElement>) => {
        updateSearchQuery(event.target.value);
    }

    return (
        <>
            <header className="stick top-0 z-50 py-2">
                <div className="container">
                    <div className="navbar px-0">
                        <div className="navbar-start">
                            <style>
                                @import url("https://fonts.googleapis.com/css2?family=Gloock&display=swap");
                            </style>

                            <a href="." className="text-3xl font-semibold" style={{ fontFamily: "Gloock, serif" }}>
                                Story Tree
                            </a>

                        </div>
                        <div className="navbar-center hidden lg:flex">
                        </div>
                        <div className="navbar-end flex flex-col sm:flex-row items-center gap-4 p-4 w-full">
                            <div className="container w-full sm:w-auto sm:flex-grow sm:max-w-2xl px-2 sm:px-5">
                                <label className="input input-bordered flex items-center gap-2 rounded-3xl w-full">
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        viewBox="0 0 16 16"
                                        fill="currentColor"
                                        className="h-4 w-4 opacity-70"
                                    >
                                        <path
                                            fillRule="evenodd"
                                            d="M9.965 11.026a5 5 0 1 1 1.06-1.06l2.755 2.754a.75.75 0 1 1-1.06 1.06l-2.755-2.754ZM10.5 7a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0Z"
                                            clipRule="evenodd"
                                        />
                                    </svg>
                                    <input type="text" className="grow" placeholder="Search" onChange={handleSearchQueryUpdate}/>

                                    <div className="relative">
                                        <button
                                            className="bi bi-filter-right hover:opacity-70 transition-opacity"
                                            onClick={() => {
                                                const modal = document.getElementById('search_filter') as HTMLDialogElement | null;
                                                if (modal) {
                                                    modal.showModal();
                                                }
                                            }}
                                        />

                                        <dialog id="search_filter" className="modal">
                                            <div className="modal-box max-w-sm md:max-w-md">
                                                <form method="dialog">
                                                    <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
                                                </form>
                                                <h3 className="font-bold text-lg pb-6">Filter search</h3>
                                                <div className="space-y-4">
                                                    <div className="grid grid-cols-2 sm:grid-cols-2 md:grid-cols-2 pb-1 pl-4">
                                                        <p className="font-medium">Genre</p>
                                                        <select className="select select-bordered select-sm w-full max-w-xs" onChange={handleGenreFilterSelect} value={genreFilterOption}>
                                                            <option>Any</option>
                                                            {
                                                                uniqueGenres.map((genre, index) => (
                                                                    <option key={index} value={genre}>
                                                                        {genre}
                                                                    </option>
                                                                ))
                                                            }
                                                        </select>
                                                    </div>
                                                    <div className="grid grid-cols-2 sm:grid-cols-2 md:grid-cols-2 pb-5 pl-4">
                                                        <p className="font-medium">Created</p>
                                                        <select className="select select-bordered select-sm w-full max-w-xs" onChange={handleDateFilterSelect} value={dateFilterOption}>
                                                            <option>Any</option>
                                                            <option>Today</option>
                                                            <option>Last week</option>
                                                            <option>Last month</option>
                                                            <option>Last year</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div className="grid grid-cols-2 sm:grid-cols-2 md:grid-cols-2">
                                                    <div className="flex justify-start">
                                                        <form method="dialog">
                                                            <button className="btn btn-sm rounded-3xl">Close</button>
                                                        </form>
                                                    </div>
                                                    <div className="flex justify-end">
                                                        <form method="dialog">
                                                            <button className="btn bg-green-600 text-white btn-sm rounded-3xl" onClick={handleConfirmFilterOption}>Confirm</button>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                            <form method="dialog" className="modal-backdrop">
                                                <button></button>
                                            </form>
                                        </dialog>
                                    </div>
                                </label>
                            </div>
                            <div className="px-10">
                                {<ThemeToggler />}
                            </div>
                            <Link className="bi bi-person text-4xl" to="/profile"></Link>
                        </div>
                    </div>
                </div>
            </header>
        </>
    )
};

export default Header;