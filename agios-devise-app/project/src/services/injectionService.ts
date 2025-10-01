import axios from 'axios';

export const exportInjection = async (
    idBmoi: string,
    dtDebutArrete: string,
    dtFinArrete: string,
    dtDernierJourOuvre: string,
    dtJourOuvreNextTrim: string
) => {
    const res = await axios.get('/api/injection/export', {
        params: {
            idBmoi,
            dtDebutArrete,
            dtFinArrete,
            dtDernierJourOuvre,
            dtJourOuvreNextTrim
        }
    });
    return res.data;
};