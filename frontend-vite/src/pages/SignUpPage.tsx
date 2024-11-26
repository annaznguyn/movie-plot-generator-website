import Header from "../components/landing/Navigation"
import SignUpForm from "../components/signup/SignUpForm"
import Footer from "../components/landing/Footer";

const SignUpPage = () => {
    return (
        <>
            <Header isSigninPage={true} page={"/signup"}/>
            <SignUpForm/>
            <Footer/>
        </>
    )
}

export default SignUpPage;