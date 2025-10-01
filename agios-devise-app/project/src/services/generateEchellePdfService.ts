import axios from 'axios';

export async function exportAllEchellePdf(
    dateDebutArrete: string,
    dateFinArrete: string,
    dateDernierJouvre: string
): Promise<{ nbtotal_comptes_traites: number, path_dossier: string }> {
    const response = await axios.get('/api/arrete-pdf/generate', {
        params: { dateDebutArrete, dateFinArrete, dateDernierJouvre }
    });
    return response.data;
}

export async function exportEchellePdfFromExcel(
    file: File,
    dateDebutArrete: string,
    dateFinArrete: string,
    dateDernierJouvre: string
): Promise<{ nbtotal_comptes_traites: number, path_dossier: string }> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('dateDebutArrete', dateDebutArrete);
    formData.append('dateFinArrete', dateFinArrete);
    formData.append('dateDernierJouvre', dateDernierJouvre);

    const response = await axios.post('/api/arrete-pdf/generate-specific', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
}