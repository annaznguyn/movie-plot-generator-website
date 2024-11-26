import { TextField } from "@mui/material"
import {styled} from "@mui/material/styles";


export const StyledTextField = styled(TextField)(({theme}) => ({
    backgroundColor: "#e6e6e6",
    '& .MuiInputLabel-root': {
        position: 'absolute',
        color: "#333333",
        left: "2%",
        transform: 'translateY(-100%)',
        pointerEvents: 'none',
    },
}));



