import { useState, useEffect } from 'react';
import axios from 'axios';
import { supabase } from '../../client/SupabaseClient';

const CharacterDrawer = ({ toggleCharacterDrawer }) => {
    
    const [characterList, setCharacterList] = useState([]);

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

    // const fetchAllCharacters = async () => {
        
    // }

    

    return (
        <div className="bg-white border-r-2 border-slate-700">
            <div className="border-2 m-2 rounded-md min-h-screen sm:w-[200px] lg:w-[400px]">
                <div className="flex justify-between">
                    <h3 className="p-5 text-slate-700 font-medium">Characters</h3>
                    <div className="justify-self-end m-2">
                        <button onClick={toggleCharacterDrawer}>
                            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="size-6">
                                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18 18 6M6 6l12 12" />
                            </svg>
                        </button>
                    </div>
                </div>
                <ul className="space-y-3">
                    {/* {characterList.map((character, index) => {
                        <li key={index} className="border-2 border-gray-600 rounded-md">
                            {character}
                        </li>
                    })} */}
                </ul>
                <div className="flex justify-center p-4">
                    <button 
                        
                        type="submit" 
                        className="btn btn-primary w-full bg-gray-800 text-white hover:bg-gray-700">
                            Add new
                    </button>
                </div>

            </div>
        </div>
    );
}

export default CharacterDrawer;