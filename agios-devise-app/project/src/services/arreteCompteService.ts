import axios from 'axios';

export const verifierSoldesArreteCompte = async (dateDebutArrete: string, dateFinArrete: string, dateDernierJouvre: string) => {
    try {
        const res = await axios.get('/api/arrete-compte/transactions/excel', {
            params: { dateDebutArrete, dateFinArrete, dateDernierJouvre }
        });
        return res.data;
    } catch (error: any) {
        if (error.code === 'ECONNABORTED') {
            throw new Error('La vérification a pris trop de temps');
        }
        throw new Error(error.response?.data?.message || 'Erreur lors de la vérification');
    }
};