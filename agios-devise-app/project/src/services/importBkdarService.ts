import axios from 'axios';

export const importBkdarService = async (mois : number, annee : number) => {
    const response = await axios.post('http://localhost:8080/api/import/bkdar', null, {
        params: { mois, annee },
        headers: {
            Accept: 'application/json'
        },
        withCredentials: true
    });
    return response;
};