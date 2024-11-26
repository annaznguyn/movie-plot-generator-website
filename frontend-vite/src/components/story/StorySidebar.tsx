import { useState, useEffect } from 'react';
import NodeDrawer from './NodeDrawer';
import CharacterDrawer from './CharacterDrawer';

const StorySidebar = () => {
    const [isNodeDrawerOpen, setIsNodeDrawerOpen] = useState(false);
    const [isCharacterDrawerOpen, setIsCharacterDrawerOpen] = useState(false);

    const toggleNodeDrawer = () => {
        setIsNodeDrawerOpen(prevState => !prevState);
        if (isCharacterDrawerOpen) {
            setIsCharacterDrawerOpen(false); 
        }
    };

    const toggleCharacterDrawer = () => {
        setIsCharacterDrawerOpen(prevState => !prevState);
        if (isNodeDrawerOpen) {
            setIsNodeDrawerOpen(false); 
        }
    };

    return (
        <>
            <div className="min-h-screen min-w-14 p-4 bg-gray-400 border-r-2 border-slate-700">
                <div className="min-h-full flex flex-col items-center space-y-4">
                    <button 
                        className={`flex justify-between gap-2 drawer-button m-1 p-1 hover:border-2 hover:border-gray-600 hover:rounded-md hover:bg-gray-200 ${isNodeDrawerOpen ? "border-2 border-gray-600 rounded-md bg-gray-200" : "border border-transparent" }`}
                        onClick={toggleNodeDrawer}
                    >
                        {/* <span className="absolute w-auto p-1 transform translate-x-1/2 translate-y-1/2 text-center text-slate-700 bg-gray-200 rounded-md opacity-0 group-hover:opacity-60 transition-opacity duration-500">
                            Node
                        </span>                     */}
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="size-6 stroke-black">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M12 6.042A8.967 8.967 0 0 0 6 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 0 1 6 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 0 1 6-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0 0 18 18a8.967 8.967 0 0 0-6 2.292m0-14.25v14.25" />
                        </svg>
                        {/* <label className="text-black">
                            Node
                        </label> */}
                    </button>

                    <button 
                        className={`flex justify-between gap-2 drawer-button m-1 p-1 hover:border-2 hover:border-gray-600 hover:rounded-md hover:bg-gray-200 ${isCharacterDrawerOpen ? "border-2 border-gray-600 rounded-md bg-gray-200" : "border border-transparent" }`}
                        onClick={toggleCharacterDrawer}
                    >
                        {/* <span className="absolute w-auto p-1 transform translate-x-1/4 translate-y-1/2 text-center text-slate-700 bg-gray-200 rounded-md opacity-0 group-hover:opacity-60 transition-opacity duration-500">
                            Characters
                        </span>   */}
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="size-6 stroke-black">
                            <path strokeLinecap="round" strokeLinejoin="round" d="M18 18.72a9.094 9.094 0 0 0 3.741-.479 3 3 0 0 0-4.682-2.72m.94 3.198.001.031c0 .225-.012.447-.037.666A11.944 11.944 0 0 1 12 21c-2.17 0-4.207-.576-5.963-1.584A6.062 6.062 0 0 1 6 18.719m12 0a5.971 5.971 0 0 0-.941-3.197m0 0A5.995 5.995 0 0 0 12 12.75a5.995 5.995 0 0 0-5.058 2.772m0 0a3 3 0 0 0-4.681 2.72 8.986 8.986 0 0 0 3.74.477m.94-3.197a5.971 5.971 0 0 0-.94 3.197M15 6.75a3 3 0 1 1-6 0 3 3 0 0 1 6 0Zm6 3a2.25 2.25 0 1 1-4.5 0 2.25 2.25 0 0 1 4.5 0Zm-13.5 0a2.25 2.25 0 1 1-4.5 0 2.25 2.25 0 0 1 4.5 0Z" />
                        </svg>
                        {/* <label className="text-black">
                            Characters
                        </label> */}
                    </button>
                </div>
            </div>
            {isNodeDrawerOpen && !isCharacterDrawerOpen &&(
                <NodeDrawer toggleNodeDrawer={toggleNodeDrawer}/>
            )}
            {isCharacterDrawerOpen && !isNodeDrawerOpen && (
                <CharacterDrawer toggleCharacterDrawer={toggleCharacterDrawer}/>
            )}
        </>
        
    );
}

export default StorySidebar;