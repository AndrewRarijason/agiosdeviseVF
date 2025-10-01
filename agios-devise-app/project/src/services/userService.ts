export interface User {
    id: number;
    username: string;
    email_bmoi: string;
    role: number | string;
    is_active: number;
}

export async function fetchUsers(): Promise<User[]> {
    const res = await fetch('/api/users');
    if (!res.ok) throw new Error('Erreur lors du chargement des utilisateurs');
    return res.json();
}

export async function addUser(user: Partial<User> & { mdp: string; role: number }): Promise<User> {
    const res = await fetch('/api/users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(user),
    });
    if (!res.ok) throw new Error('Erreur lors de l\'ajout');
    return res.json();
}

export async function updateUser(id: number, user: Partial<User>): Promise<User> {
    const res = await fetch(`/api/users/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(user),
    });
    if (!res.ok) throw new Error('Erreur lors de la modification');
    return res.json();
}

export async function deleteUser(id: number): Promise<void> {
    const res = await fetch(`/api/users/${id}`, { method: 'DELETE' });
    if (!res.ok) throw new Error('Erreur lors de la suppression');
}

export interface Role {
    id: number;
    name: string;
}

export async function fetchRoles(): Promise<Role[]> {
    const res = await fetch('/api/roles');
    if (!res.ok) throw new Error('Erreur lors du chargement des rôles');
    return res.json();
}