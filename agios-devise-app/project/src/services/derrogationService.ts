import axios from 'axios';

export async function importDerrogation(file: File, dateDebutNextTrim: string) {
    const formData = new FormData();
    formData.append('file', file);

    try {
        const res = await axios.post('/api/import-derrogation', formData, {
            params: { dateDebutNextTrim },
            headers: { 'Content-Type': 'multipart/form-data' }
        });
        return { ok: res.status >= 200 && res.status < 300, data: res.data };
    } catch (error: any) {
        // Récupère le message d'erreur du backend si présent
        return {
            ok: false,
            data: { error: error?.response?.data?.error || error.message }
        };
    }
}

export async function deleteDerogation(): Promise<{ ok: boolean; message: string }> {
    const res = await fetch(`/api/derogations`, { method: 'DELETE' });
    const data = await res.json();
    if (!res.ok) throw new Error(data?.message || 'Erreur lors de la suppression des dérogations');
    return { ok: true, message: data.message };
}
