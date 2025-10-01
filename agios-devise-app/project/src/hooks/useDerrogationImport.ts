import { useState } from 'react';
import { importDerrogation } from '../services/derrogationService';

export function useDerrogationImport() {
    const [derroFile, setDerroFile] = useState<File | null>(null);
    const [derroMessage, setDerroMessage] = useState('');
    const [derroStatus, setDerroStatus] = useState<'success' | 'error' | ''>('');
    const [isImportingDerro, setIsImportingDerro] = useState(false);

    const handleDerroFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) setDerroFile(e.target.files[0]);
    };

    const handleImportDerrogation = async (dateDebutNextTrim: string, file?: File) => {
        const importFile = file || derroFile;
        if (!importFile || !dateDebutNextTrim) {
            setDerroMessage("Veuillez sélectionner un fichier et une date.");
            setDerroStatus('error');
            return;
        }
        setIsImportingDerro(true);
        setDerroMessage('');
        try {
            const { ok, data } = await importDerrogation(importFile, dateDebutNextTrim);
            if (ok) {
                setDerroMessage(`Nombre de dérogations chargées en mémoire: ${data.comptes_derogation_charges}. | Fichier exporté: ${data.fichier_derogations_colore}`);
                setDerroStatus('success');
            } else {
                setDerroMessage(data.error || "Erreur lors de l'import.");
                setDerroStatus('error');
            }
        } catch (e: unknown) {
            setDerroMessage(e instanceof Error ? e.message : "Erreur lors de l'import.");
            setDerroStatus('error');
        }
        setIsImportingDerro(false);
    };


    return {
        derroFile,
        setDerroFile,
        derroMessage,
        derroStatus,
        isImportingDerro,
        handleDerroFileChange,
        handleImportDerrogation,
    };
}