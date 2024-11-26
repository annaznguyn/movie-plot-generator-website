import React from 'react';

export default function Footer() {
    return (
        <>
            <footer className="footer bg-neutral text-neutral-content p-10">
                <nav>
                    <h6 className="footer-title">Other services (coming soon)</h6>
                    <a className="link link-hover">Writing courses</a>
                    <a className="link link-hover">Professional editing</a>
                    <a className="link link-hover">Publication</a>
                    <a className="link link-hover">Screenwriting events</a>
                </nav>
                <nav>
                    <h6 className="footer-title">Company</h6>
                    <a className="link link-hover">About us</a>
                    <a className="link link-hover">Contact</a>
                    <a className="link link-hover">Contribute</a>
                </nav>
                <nav>
                    <h6 className="footer-title">Legal</h6>
                    <a className="link link-hover">Terms of use</a>
                    <a className="link link-hover">Privacy policy</a>
                    <a className="link link-hover">Cookie policy</a>
                </nav>
            </footer>
            <div className="bg-neutral text-neutral-content pb-8">
                <p className="text-center text-sm pb-2">
                    &copy; {new Date().getFullYear()} Story Tree. All rights reserved.
                </p>
                <p className="text-center text-sm">
                    Made by L10G09WED-ELEC5619.
                </p>
            </div>
        </>
    );
}