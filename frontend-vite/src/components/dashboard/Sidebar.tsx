import React, { ChangeEvent, MouseEvent, useState, useEffect } from "react";
import { supabase } from "../../client/SupabaseClient";
import axios from "axios";

export interface SidebarProps {
    handleStoryCreation: (title: string, summary: string, genre: string) => void;
    refreshStories: () => void;
};

const baseURL: string = import.meta.env.VITE_BASE_URL;

const Sidebar: React.FC<SidebarProps> = ({ handleStoryCreation, refreshStories }) => {
    const [title, setTitle] = useState("");
    const [summary, setSummary] = useState("");
    const [genre, setGenre] = useState("");


    const handleConfirmStoryCreation = async () => {
        try {
            await addNewStory();
    
            setTitle("");
            setSummary("");
            setGenre("");
    
            const modal = document.getElementById('create_form') as HTMLDialogElement | null;
            if (modal) {
                modal.close();
            }
    
            await new Promise(resolve => setTimeout(resolve, 100));
    
            await refreshStories();
    
        } catch (error) {
            console.error('Error creating story:', error);
        }
    };

    const handleTitleUpdate = (event: React.ChangeEvent<HTMLInputElement>) => {
        setTitle(event.target.value);
    }

    const handleSummaryUpdate = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
        setSummary(event.target.value);
    }

    const handleGenreUpdate = (event: React.ChangeEvent<HTMLInputElement>) => {
        setGenre(event.target.value);
    }

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

    const addNewStory = async () => {
        const user = await getSessionUser();
        console.log(user);
        const email = user?.email;
        console.log(email);

        console.log(title);
        console.log(genre);
        console.log(summary);
        axios.post(baseURL + "/stories", {
            email: email,
            storyname: title,
            genre: genre,
            description: summary
        })
            .then(response => {
                console.log(response.data);
            })
            .catch(error => {
                console.log(error.message);
            })
    }


    return (
        <>
            <li className="rounded-4xl">
                <a>
                    <i className="bi bi-house-door"></i> Home
                </a>
            </li>
            <li className="rounded-4xl">
                <a>
                    <i className="bi bi-clock"></i> Recent
                </a>
            </li>
            <li>
                <a
                    onClick={() => {
                        const modal = document.getElementById('create_form') as HTMLDialogElement | null;
                        if (modal) {
                            modal.showModal();
                        }
                    }}
                >
                    <i className="bi bi-plus-square"></i> Create new
                </a>
            </li>
            <div className="relative">
                <dialog id="create_form" className="modal">
                    <div className="modal-box max-w-sm md:max-w-md">
                        <form method="dialog">
                            <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
                        </form>
                        <h3 className="font-bold text-lg pb-6 text-center">Create a new story</h3>
                        <div className="space-y-4 pb-8">
                            <input
                                type="text"
                                placeholder="Enter your title here"
                                className="input input-bordered w-full rounded-3xl"
                                onChange={handleTitleUpdate}
                                value={title} />
                            <textarea
                                placeholder="Enter a short summary of the story (less than 80 characters)."
                                className="textarea textarea-bordered textarea-md w-full rounded-3xl text-md h-32"
                                onChange={handleSummaryUpdate}
                                value={summary}>
                            </textarea>
                            <input
                                type="text"
                                placeholder="Enter the genre"
                                className="input input-bordered input-md w-full rounded-3xl"
                                onChange={handleGenreUpdate}
                                value={genre} />
                        </div>
                        <div className="grid grid-cols-2 sm:grid-cols-2 md:grid-cols-2">
                            <div className="flex justify-start">
                                <form method="dialog">
                                    <button className="btn btn-sm rounded-3xl">Close</button>
                                </form>
                            </div>
                            <div className="flex justify-end">
                                <form method="dialog">
                                    <button className="btn bg-green-600 text-white btn-sm rounded-3xl" onClick={handleConfirmStoryCreation}>Confirm</button>
                                </form>
                            </div>
                        </div>
                    </div>
                    <form method="dialog" className="modal-backdrop">
                        <button></button>
                    </form>
                </dialog>
            </div>

        </>
    );
}

export default Sidebar;