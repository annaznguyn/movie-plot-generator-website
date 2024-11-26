import React, { useState, MouseEvent } from "react";

interface SortOptionProps {
    onSortOptionChange: (data: string) => void;
};

const DashboardHeader: React.FC<SortOptionProps> = ({ onSortOptionChange }) => {
    const [selectedOption, setSelectedOption] = useState("Title");

    const handleDropdownClick = (event: MouseEvent<HTMLAnchorElement>) => {
        const selectedValue = event.currentTarget.textContent;
        if (selectedValue) {
            setSelectedOption(selectedValue);
            onSortOptionChange(selectedValue);
        }
    };

    return (
        <>
            <h2 className="text-3xl font-bold">Dashboard</h2>
            <div>
                <div className="dropdown dropdown-hover dropdown-end">
                    <div tabIndex={0} role="button" className="btn btn-primary btn-sm m-1 h-auto border-transparent rounded-3xl">
                        <i className="bi bi-arrow-down"></i> Sort by: {selectedOption} </div>
                    <ul tabIndex={0} className="dropdown-content menu bg-base-200 rounded-box z-[1] w-52 p-2 shadow">
                        <li><a onClick={handleDropdownClick}>Title</a></li>
                        <li><a onClick={handleDropdownClick}>Created</a></li>
                        <li><a onClick={handleDropdownClick}>Most recent</a></li>
                        <li><a onClick={handleDropdownClick}>Genre</a></li>
                    </ul>
                </div>
            </div>

        </>
    );
}

export default DashboardHeader;