import axios from 'axios';

export const importTicDevise = async (file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    const response = await axios.post('/api/import-tx-crediteur', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    });

    // Vérifie si la réponse contient une erreur métier
    if (
        typeof response.data === 'string' &&
        response.data.includes('Erreur lors de l\'import')
    ) {
        throw new Error(response.data);
    }
    if (
        typeof response.data === 'object' &&
        response.data.message &&
        response.data.message.includes('Erreur lors de l\'import')
    ) {
        throw new Error(response.data.message);
    }

    return response.data;
};

export const fetchTicDevises = async () => {
    const res = await axios.get('/api/visualiser-tx-crediteur');
    return res.data;
};