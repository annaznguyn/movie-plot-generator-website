import React from "react";
import ThemeToggler from "./ThemeToggler";
import { Link } from 'react-router-dom';


export default function Header({ isSigninPage, page }: { isSigninPage: boolean, page: any }) {
    let buttonText;
    if (page === "/signin") {
        buttonText = "Sign up"
        page = "/signup";
    }
    else if (page === "/signup") {
        buttonText = "Sign in";
        page = "/signin";
    }
    else {
        buttonText = "Sign in";
    }
    return (
        <>
            <header className="stick top-0 z-50 py-2">
                <div className="container">
                    <div className="navbar px-0">
                        <div className="navbar-start">
                            <div className="dropdown">
                                <div tabIndex={0} role="button" className="btn btn-primary btn-circle lg:hidden mr-1">
                                    <i className="bi bi-list text-2xl"></i>
                                </div>
                                <ul
                                    tabIndex={0}
                                    className="menu menu-sm dropdown-content mt-1 p-2 shadow bg-base-200 rounded-box z-[1] mt-3 w-52 p-2 shadow">
                                    <li><a href=".">Home</a></li>
                                    <li><a href="#!">About</a></li>
                                    <li><a href="#!">Products</a></li>
                                    <li><a href="#!">Pricing</a></li>
                                    <li><a href="#!">Contact Us</a></li>
                                </ul>
                            </div>
                            <style>
                            @import url("https://fonts.googleapis.com/css2?family=Gloock&display=swap");
                            </style>

                            <a href="." className="text-3xl font-semibold" style={{fontFamily: "Gloock, serif"}}>
                                Story Tree
                            </a>
                        </div>
                        <div className="navbar-center hidden lg:flex">
                            <ul className="menu menu-horizontal px-1 font-medium">
                                <li><a href=".">Home</a></li>
                                <li><a href="#!">About</a></li>
                                <li><a href="#!">Products</a></li>
                                <li><a href="#!">Pricing</a></li>
                                <li><a href="#!">Contact Us</a></li>
                            </ul>
                        </div>
                        <div className="navbar-end">
                            <div className="px-20">
                                {isSigninPage ? null : <ThemeToggler />}
                            </div>
                            {isSigninPage ? <Link to={page} className="btn btn-outline">{buttonText}</Link> 
                            : <Link to="/signin" className="btn btn-outline">Sign in</Link>}
                        </div>
                    </div>
                </div>
            </header>
        </>
    )
}