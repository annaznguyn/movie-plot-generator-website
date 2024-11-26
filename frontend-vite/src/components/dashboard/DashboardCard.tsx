import React, { useState, MouseEvent } from "react";

interface DashboardCardProps {
    id: number;
    title: string;
    image: string;
    summary: string;
    created: Date;
    modified: Date;
    genre: string;
    onUpdate: (id: number, title: string, summary: string, genre: string) => void;
    onDelete: (id: number) => void;
}

function formatDate(arg: Date): string {
    const [day, month, year] = arg.toLocaleDateString().split('/').map(Number);
    const date = new Date(year, month - 1, day);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
    });
}

export default function DashboardCard({ id, title, image, summary, created, modified, genre, onUpdate, onDelete }: DashboardCardProps) {
    const [editedTitle, setEditedTitle] = useState(title);
    const [editedSummary, setEditedSummary] = useState(summary);
    const [editedGenre, setEditedGenre] = useState(genre);

    const handleSaveChanges = () => {
        onUpdate(id, editedTitle, editedSummary, editedGenre);
    };

    const handleDelete = () => {
        const modal = document.getElementById(`delete_confirmation_${id}`) as HTMLDialogElement | null;
        if (modal) {
            modal.showModal();
        }
    };

    const handleEdit = () => {
        const modal = document.getElementById(`edit_form_${id}`) as HTMLDialogElement | null;
        if (modal) {
            modal.showModal();
        }
    };

    const handleConfirmDelete = () => {
        onDelete(id);
    };

    return (
        <>
            <div className="card card-compact bg-base-100">
                <figure className="h-40 overflow-hidden">
                    <img src={image} alt="Illustration" />
                </figure>
                <div className="card-body">
                    <h2 className="card-title">{title}</h2>
                    <p>{summary}</p>
                    <p className="mb-1"><b>Genre:</b> {genre}</p>
                    <p className="mb-1"><b>Created:</b> {formatDate(created)}</p>
                    <p className="mb-1"><b>Edited:</b> {formatDate(modified)}</p>
                    <div className="flex flex-col gap-4 p-4">
                        <div className="card-actions w-full">
                            <button
                                onClick={() => {/* Jian change to your story tree page here */ }}
                                className="btn bg-teal-600 text-white rounded-3xl hover:bg-teal-700 transition-colors duration-300 flex items-center justify-center gap-2 w-full"
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                                </svg>
                                Visit
                            </button>
                        </div>
                        <div className="card-actions w-full">
                            <button
                                onClick={handleEdit}
                                className="btn btn-neutral rounded-3xl hover:bg-gray-700 transition-colors duration-300 flex items-center justify-center gap-2 w-full"
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                                </svg>
                                Edit
                            </button>
                        </div>
                        <div className="card-actions w-full">
                            <button
                                onClick={handleDelete}
                                className="btn btn-outline btn-error rounded-3xl hover:bg-red-500 hover:text-white transition-colors duration-300 flex items-center justify-center gap-2 w-full"
                            >
                                <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z" />
                                </svg>
                                Delete
                            </button>
                        </div>
                    </div>
                </div>
            </div>


            <dialog id={`edit_form_${id}`} className="modal">
                <div className="modal-box max-w-sm md:max-w-md">
                    <form method="dialog">
                        <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
                    </form>
                    <h3 className="font-bold text-lg pb-6 text-center">Edit story</h3>
                    <div className="space-y-4 pb-8">
                        <input
                            type="text"
                            placeholder="Enter your title here"
                            className="input input-bordered w-full rounded-3xl"
                            value={editedTitle}
                            onChange={(e) => setEditedTitle(e.target.value)}
                        />
                        <textarea
                            placeholder="Enter a short summary of the story (less than 80 characters)."
                            className="textarea textarea-bordered textarea-md w-full rounded-3xl text-md h-32"
                            value={editedSummary}
                            onChange={(e) => setEditedSummary(e.target.value)}
                        >
                        </textarea>
                        <input
                            type="text"
                            placeholder="Enter the genre"
                            className="input input-bordered input-md w-full rounded-3xl"
                            value={editedGenre}
                            onChange={(e) => setEditedGenre(e.target.value)}
                        />
                    </div>
                    <div className="grid grid-cols-2 sm:grid-cols-2 md:grid-cols-2">
                        <div className="flex justify-start">
                            <form method="dialog">
                                <button className="btn btn-sm rounded-3xl">Cancel</button>
                            </form>
                        </div>
                        <div className="flex justify-end">
                            <form method="dialog">
                                <button
                                    className="btn bg-green-600 text-white btn-sm rounded-3xl"
                                    onClick={handleSaveChanges}
                                >
                                    Save Changes
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
                <form method="dialog" className="modal-backdrop">
                    <button></button>
                </form>
            </dialog>

            <dialog id={`delete_confirmation_${id}`} className="modal">
                <div className="modal-box max-w-sm">
                    <form method="dialog">
                        <button className="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
                    </form>
                    <h3 className="font-bold text-lg pb-4">Delete Story</h3>
                    <p className="py-4">Are you sure you want to delete "{title}"? This action cannot be undone.</p>
                    <div className="grid grid-cols-2 gap-4">
                        <div className="flex justify-start">
                            <form method="dialog">
                                <button className="btn btn-sm rounded-3xl">Cancel</button>
                            </form>
                        </div>
                        <div className="flex justify-end">
                            <form method="dialog">
                                <button
                                    className="btn btn-error text-white btn-sm rounded-3xl"
                                    onClick={handleConfirmDelete}
                                >
                                    Delete
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
                <form method="dialog" className="modal-backdrop">
                    <button></button>
                </form>
            </dialog>
        </>
    );
}