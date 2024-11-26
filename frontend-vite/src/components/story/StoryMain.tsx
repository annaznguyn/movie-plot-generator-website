import StorySidebar from "./StorySidebar";
import axios from "axios";
import { supabase } from "../../client/SupabaseClient";
import Node from "./Node";
import InfiniteCanvas from "./InfiniteCanvas";

const baseURL = import.meta.env.VITE_BASE_URL;

interface Node {
    id: number;
    nodeName: string;
    description: string;
    genre: string;
    result: string;
    characters: string[];
}

const StoryMain = () => {

    const fetchStory = async (storyId: string) => {

        axios.get(baseURL + `/stories/${storyId}`)
        .then(response => {
            console.log(response);
        })
        .catch(error => {
            console.log(error.message);
        })
        
    };
    
    return (
        <>
            <div className="flex">
                <div className="min-h-screen min-w-full">
                    <InfiniteCanvas/>
                </div>
            </div>
        </>
    );
}

export default StoryMain;