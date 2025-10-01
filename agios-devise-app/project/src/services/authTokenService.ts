export async function getUserRole(): Promise<string | null> {
    const res = await fetch('/validate-token', {
        method: 'POST',
        credentials: 'include'
    });
    const data = await res.json();
    return data.role || null;
}
