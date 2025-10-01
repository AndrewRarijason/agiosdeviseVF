import axios from 'axios';

export const exportBkhisCsv = async (dateRef: string, dateFin: string) => {
    return await axios.get(`http://localhost:8080/api/extraction/bkhis/csv`, {
        params: { dateref: dateRef, datefin: dateFin },
        responseType: 'blob',
        headers: { Accept: 'text/csv' },
        withCredentials: true
    });
};

export const exportBkhisExcel = async (dateRef: string, dateFin: string) => {
    return await axios.get(`http://localhost:8080/api/extraction/bkhis/xlsx`, {
        params: { dateref: dateRef, datefin: dateFin },
        responseType: 'blob',
        headers: { Accept: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' },
        withCredentials: true
    });
};