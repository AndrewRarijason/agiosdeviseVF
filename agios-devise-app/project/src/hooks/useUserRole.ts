import { useEffect, useState } from 'react';
import { getUserRole } from '../services/authTokenService';

export function useUserRole() {
    const [role, setRole] = useState<string | null>(null);

    useEffect(() => {
        getUserRole()
            .then(setRole)
            .catch(() => setRole(null));
    }, []);

    return role;
}
