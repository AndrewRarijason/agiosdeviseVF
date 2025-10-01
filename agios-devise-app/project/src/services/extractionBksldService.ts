import axios from 'axios';

export const exportBksldCsv = async (date: string) => {
    const response = await axios.get(`http://localhost:8080/api/extraction/bksld/csv`, {
        params: { date },
        responseType: 'blob',
        headers: {
            Accept: 'text/csv'
        },
        withCredentials: true
    });
    return response;
};

export const exportBksldExcel = async (date: string) => {
    const response = await axios.get(`http://localhost:8080/api/extraction/bksld/xlsx`, {
        params: { date },
        responseType: 'blob',
        headers: {
            Accept: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        },
        withCredentials: true
    });
    return response;
};