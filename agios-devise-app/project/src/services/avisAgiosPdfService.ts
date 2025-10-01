import axios from 'axios';

export async function exportAllAvisAgiosPdf(dateDebut: string, dateFin: string): Promise<{ nbtotal_comptes_traites: number, path_dossier: string }> {
    const response = await axios.get('/api/avis-agios-pdf/export-all', {
        params: { dateDebut, dateFin }
    });
    return response.data;
}

export async function exportAvisAgiosFromExcel(file: File, dateDebut: string, dateFin: string): Promise<{ nbtotal_comptes_traites: number, path_dossier: string }> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('dateDebut', dateDebut);
    formData.append('dateFin', dateFin);

    const response = await axios.post('/api/avis-agios-pdf/export-from-excel', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
}