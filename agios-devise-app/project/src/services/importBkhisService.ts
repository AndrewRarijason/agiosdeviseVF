import axios from 'axios';

export const importBkhisService = async (dateRef: string, dateFin: string) => {
    const response = await axios.post('http://localhost:8080/api/import/bkhis', null, {
        params: { dateref: dateRef, datefin: dateFin },
        headers: {
            Accept: 'application/json'
        },
        withCredentials: true
    });
    return response;
};