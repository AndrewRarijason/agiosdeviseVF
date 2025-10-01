import axios from 'axios';

export async function getSumAgiosParTcli(dateDebutArrete: string, dateFinArrete: string) {
    const res = await axios.get('/api/agios-tcli', {
        params: { dateDebutArrete, dateFinArrete }
    });
    return res.data;
}