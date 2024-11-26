import React from "react";
import ThemeToggler from "./ThemeToggler";
import { Link } from 'react-router-dom';
import Dashboard from "../../pages/Dashboard";


export default function Header({ isSigninPage }: { isSigninPage: boolean }) {
    let buttonText;

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
                                    <li><Link to="/dashboard" className="hover:text-primary">Dashboard</Link></li>
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
                                <li><Link to="/dashboard">Dashboard</Link></li>
                                <li><a href="#!">Contact Us</a></li>
                            </ul>
                        </div>
                        <div className="navbar-end">
                            <ThemeToggler/>
                        </div>
                    </div>
                </div>
            </header>
        </>
    )
}