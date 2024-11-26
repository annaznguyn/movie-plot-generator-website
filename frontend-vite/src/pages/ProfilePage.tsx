import React, { useState, useEffect } from "react";
import { supabase } from "../client/SupabaseClient";
import axios from "axios";
import Navigation from "../components/profile/Navigation";
import MainSection from "../components/profile/MainSection";
import Footer from "../components/profile/Footer";

const ProfilePage: React.FC = () => {
    return (
        <>
            <Navigation isSigninPage={false}/>
            <MainSection/>
            <Footer/>
        </>
    );
};

export default ProfilePage;