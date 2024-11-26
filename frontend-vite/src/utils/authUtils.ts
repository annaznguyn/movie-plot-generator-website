import { supabase } from "../client/SupabaseClient";

export const getSessionUser = async () => {
    const { data, error } = await supabase.auth.getSession();

    if (error) {
        console.error(error.message);
        return null;
    }

    console.log(data.session);

    const session = data.session;

    if (session) {
        return session.user;
    }

    return null;
}
