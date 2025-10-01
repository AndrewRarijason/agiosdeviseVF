import { useEffect, useState } from 'react';
import { Navigate, useLocation } from 'react-router-dom';

type Props = {
    children: React.ReactNode;
    requiredRole?: string;
    excludedRole?: string;
};

const PrivateRoute = ({ children, requiredRole, excludedRole }: Props) => {
    const [authState, setAuthState] = useState<{
        isAuth: boolean;
        role?: string;
    } | null>(null);
    const location = useLocation();

    useEffect(() => {
        fetch('/validate-token', {
            method: 'POST',
            credentials: 'include'
        })
            .then(res => res.json())
            .then(data => setAuthState({
                isAuth: !!data.valid,
                role: data.role
            }))
            .catch(() => setAuthState({ isAuth: false }));
    }, []);

    if (authState === null) return null; // Loader possible

    if (!authState.isAuth) {
        return <Navigate to="/" replace state={{ from: location }} />;
    }

    // Blocage si le rôle est exclu
    if (excludedRole && authState.role === excludedRole) {
        return <Navigate to="/accueil" replace state={{ from: location }} />;
    }

    // Blocage si le rôle requis n'est pas présent
    if (requiredRole && authState.role !== requiredRole) {
        return <Navigate to="/accueil" replace state={{ from: location }} />;
    }

    return <>{children}</>;
};

export default PrivateRoute;