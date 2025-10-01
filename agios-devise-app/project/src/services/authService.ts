export const login = async (username: string, password: string) => {
    const formData = new FormData();
    formData.append('username', username);
    formData.append('password', password);

    const response = await fetch('/login', {
        method: 'POST',
        body: formData,
        credentials: 'include'
    });

    if (response.status === 401) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Identifiants incorrects');
    }

    if (!response.ok) {
        throw new Error('Erreur serveur');
    }

    return response.json();
};