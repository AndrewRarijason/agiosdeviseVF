import React, { useEffect, useState } from 'react';

const UserInfo: React.FC = () => {
    const [user, setUser] = useState<{ username?: string; role?: string }>({});

    useEffect(() => {
        fetch('/validate-token', {
            method: 'POST',
            credentials: 'include'
        })
            .then(res => res.json())
            .then(data => setUser({ username: data.username, role: data.role }))
            .catch(() => setUser({}));
    }, []);

    if (!user.username || !user.role) return null;

    return (
        <div className="absolute top-4 left-4 z-50 bg-white/80 text-[#581D74] px-4 py-2 rounded shadow text-sm font-semibold">
            {user.username} votre rôle est {user.role}
        </div>
    );
};

export default UserInfo;