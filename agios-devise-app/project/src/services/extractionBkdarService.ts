import axios from 'axios';

export const exportBkdarCsv = async (mois: number, annee: number) => {
    return await axios.get(`http://localhost:8080/api/extraction/bkdar/csv`, {
        params: { mois, annee },
        responseType: 'blob',
        headers: { Accept: 'text/csv' },
        withCredentials: true
    });
};

export const exportBkdarExcel = async (mois: number, annee: number) => {
    return await axios.get(`http://localhost:8080/api/extraction/bkdar/xlsx`, {
        params: {mois, annee},
        responseType: 'blob',
        headers: { Accept: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'},
        withCredentials: true
    });
}