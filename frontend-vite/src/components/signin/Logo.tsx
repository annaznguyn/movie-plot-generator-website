import React from "react";
import {Avatar} from "@mui/material";
import logo from "../../assets/login/logo.png";

interface NarrativeLogoProps {
    size?: number;
}

const NarrativeLogo: React.FC<NarrativeLogoProps> = ({ size = 50 }) => {
    return (
        <Avatar
            alt="Logo"
            src={logo}
            sx={{
                width: size,
                height: size,
                alignItems: "left"
            }}
        />
    );
}

export default NarrativeLogo;