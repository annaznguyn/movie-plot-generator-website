import React from "react"
import SignInForm from "../components/signin/SignInForm"
import Header from "../components/landing/Navigation"
import Footer from "../components/landing/Footer";

const SignInPage = () => {
    return (
        <>
            <Header isSigninPage={true} page={"/signin"}/>
            <SignInForm/>
            <Footer/>
        </>
        
    );
};

export default SignInPage;