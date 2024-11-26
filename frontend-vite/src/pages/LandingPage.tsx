import React from "react";

import Header from "../components/landing/Navigation";
import FaceSlapper from "../components/landing/FaceSlapper";
import Services from "../components/landing/Services";
import Footer from "../components/landing/Footer";

const LandingPage = () => {
    return (
        <>
          <Header isSigninPage={false} page={"/"}/>
          <FaceSlapper/>
          <Services/>
          <Footer/>
        </>
      );
}

export default LandingPage;