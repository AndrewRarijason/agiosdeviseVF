import axios from 'axios';

export const getDashboardArrete = async (dateDebutArrete: string, dateFinArrete: string) => {
    const res = await axios.get('/api/dashboard-arrete', {
        params: { dateDebutArrete, dateFinArrete }
    });
    return res.data;
};

export const getRepartitionAgios = async (dateDebutArrete: string, dateFinArrete: string) => {
    const res = await axios.get('/api/repartition-agios', {
    params: {dateDebutArrete, dateFinArrete}
    });
    return res.data;
};