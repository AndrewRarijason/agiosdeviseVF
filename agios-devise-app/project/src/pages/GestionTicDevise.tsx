import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Footer from '../components/Footer';
import { ChevronLeft } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { useTicDevise } from '../hooks/useTicDevise';
import Header from "../components/Header.tsx";

interface TicDevise {
    cdAlpha: string;
    cdIso: string;
    libele: string;
    valeur: number;
}

const GestionTicDevise: React.FC = () => {
    const navigate = useNavigate();
    const {
        loading,
        ticData,
        handleVisualiser
    } = useTicDevise();

    const [cdIsoFilter, setCdIsoFilter] = useState('');

    useEffect(() => {
        handleVisualiser();
    }, []);

    const handleBack = () => {
        navigate('/calcul-arretes');
    };

    // Cast ticData en TicDevise[]
    const ticDevises = ticData as TicDevise[];

    // Filtrage par cdIso
    const filteredData = cdIsoFilter
        ? ticDevises.filter((row) => row.cdIso.includes(cdIsoFilter))
        : ticDevises;

    return (
        <div className="min-h-screen bg-gray-100 flex flex-col">
            <div className="flex flex-1">
                <Sidebar currentPage="Gestion taux créditeurs" />
                <div className="flex-1 ml-64 transition-all duration-300">
                    <Header title="TAUX CRÉDITEURS PAR DEVISE" />
                    <main className="p-8">
                        <div className="max-w-4xl mx-auto space-y-8">
                            <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                                <div className="flex items-center gap-4">
                                    <input
                                        type="text"
                                        placeholder="Rechercher par code numérique"
                                        value={cdIsoFilter}
                                        onChange={e => setCdIsoFilter(e.target.value)}
                                        className="w-60 px-3 py-2 border border-gray-300 rounded-lg text-sm"
                                    />
                                </div>
                                <button
                                    onClick={handleBack}
                                    className="flex items-center px-4 py-2 bg-[#581D74] text-white rounded-lg hover:bg-[#581D74]/90"
                                >
                                    <ChevronLeft className="w-5 h-5 mr-2" />
                                    Retour
                                </button>
                            </div>
                            <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
                                <table className="min-w-full text-xs">
                                    <thead>
                                    <tr>
                                        <th className="px-4 py-2 text-left">CODE ALPHA</th>
                                        <th className="px-4 py-2 text-left">CODE NUMÉRIQUE</th>
                                        <th className="px-4 py-2 text-left">LIBELLÉ</th>
                                        <th className="px-4 py-2 text-left">VALEUR</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {filteredData.length === 0 && !loading && (
                                        <tr>
                                            <td colSpan={4} className="text-center py-4 text-gray-500">Aucune donnée</td>
                                        </tr>
                                    )}
                                    {filteredData.map((row, idx) => (
                                        <tr key={idx}>
                                            <td className="px-4 py-2">{row.cdAlpha}</td>
                                            <td className="px-4 py-2">{row.cdIso}</td>
                                            <td className="px-4 py-2">{row.libele}</td>
                                            <td className="px-4 py-2">{row.valeur}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                            <button
                                onClick={handleBack}
                                className="flex items-center px-4 py-2 bg-[#581D74] text-white rounded-lg hover:bg-[#581D74]/90"
                            >
                                <ChevronLeft className="w-5 h-5 mr-2" />
                                Retour
                            </button>
                        </div>
                    </main>
                    <footer className="bg-white border-t border-gray-200 px-8 py-6 mt-12">
                        <div className="text-center">
                            <p className="text-gray-500 text-sm font-medium">
                                © 2025 BMOI - Tous droits réservés
                            </p>
                        </div>
                    </footer>
                </div>
            </div>
        </div>
    );
};

export default GestionTicDevise;