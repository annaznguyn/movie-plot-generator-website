import React from "react";

import { Link } from "react-router-dom";

export default function FaceSlapper() {
    return (
        <section>
            <div className="container pt-10">
                <div
                    className="hero h-96 md:h-[500px] rounded-box overflow-hidden"
                    style={{
                        backgroundImage: "url(https://cdn.myportfolio.com/a7dcc6d5ac1134b2d40ac6d1c5552304/1f0a0456-b934-4f28-beee-c47de7308667_rw_1920.gif?h=5ecc8b473e70030b5e3d8b8ef826ca1c)",
                    }}>
                    <div className="hero-overlay bg-opacity-70"></div>
                    <div style={{ color: "#fff" }} className="hero-content text-neutral-content text-center">
                        <div className="max-w-md">
                            <h1 className="mb-5 text-5xl font-bold">Create your perfect world</h1>
                            <p className="mb-5">
                            Imagine infinite happy endings for your stories.
                            </p>
                            <Link to="/signup" className="btn border-2 border-transparent 
                                            bg-green-600 text-white hover:bg-green-700 
                                            hover:border-green-700 active:border-green-800 
                                            active:bg-green-800 w-40">
                                        Get Started
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    )
}