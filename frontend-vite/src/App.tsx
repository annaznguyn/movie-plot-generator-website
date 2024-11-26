import React, { Profiler } from "react";
import "bootstrap-icons/font/bootstrap-icons.css";
import "./App.css";

import {BrowserRouter as Router, Route, Routes, Link} from 'react-router-dom'

import LandingPage from "./pages/LandingPage";
import SignInPage from "./pages/SignInPage";
import SignUpPage from "./pages/SignUpPage"
import StoryPage from "./pages/StoryPage";
import Dashboard from "./pages/Dashboard";
import Profile from "./pages/ProfilePage";

function App() {
  return (
    <>
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage/>}></Route>
        <Route path="/signin" element={<SignInPage/>}></Route>
        <Route path="/signup" element={<SignUpPage/>}></Route>
        <Route path="/story" element={<StoryPage/>}></Route>
        <Route path="/dashboard" element={<Dashboard/>}></Route>
        <Route path="/profile" element={<Profile/>}></Route>
      </Routes>
    </Router>
    </>
  );
}

export default App;
