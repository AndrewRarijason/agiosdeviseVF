import React, { useState, useEffect } from 'react';
import Sidebar from '../components/Sidebar';
import Header from '../components/Header';
import { useHistoriqueArretes } from '../hooks/useHistoriqueArretes';
import { ChevronLeft, Search, FileText, BookOpen, ChevronRight } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { Trimestre } from '../types';
import Footer from "../components/Footer.tsx";

const trimestres: { label: Trimestre; value: Trimestre }[] = [
    { label: 'T1', value: 'T1' },
    { label: 'T2', value: 'T2' },
    { label: 'T3', value: 'T3' },
    { label: 'T4', value: 'T4' },
];

type HistoriqueArrete = {
    ncp: string;
    nomrest: string;
    dev: string;
    soldeFinalCalcule: number;
    netAgios: number;
};

const HistoriqueArretes: React.FC = () => {
    const [trimestre, setTrimestre] = useState<Trimestre>('T1');
    const [annee, setAnnee] = useState(new Date().getFullYear());
    const [searchNcp, setSearchNcp] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const { data, loading, error, getHistorique } = useHistoriqueArretes();
    const navigate = useNavigate();

    const periodeTrimestre: Record<Trimestre, { debut: string; fin: string }> = {
        T1: { debut: `${annee}-01-01`, fin: `${annee}-03-31` },
        T2: { debut: `${annee}-04-01`, fin: `${annee}-06-30` },
        T3: { debut: `${annee}-07-01`, fin: `${annee}-09-30` },
        T4: { debut: `${annee}-10-01`, fin: `${annee}-12-31` },
    };

    const periode = periodeTrimestre[trimestre];

    const handleBack = () => {
        navigate('/accueil');
    };

    const handleSearch = (e: React.FormEvent) => {
        e.preventDefault();
        getHistorique(trimestre, annee);
        setCurrentPage(1);
    };

    // Filtrage par NCP
    const filteredData = searchNcp
        ? data.filter((item: HistoriqueArrete) =>
            item.ncp.toLowerCase().includes(searchNcp.toLowerCase()) ||
            item.nomrest.toLowerCase().includes(searchNcp.toLowerCase())
        )
        : data;

    // Pagination
    const itemsPerPage = 20;
    const totalPages = Math.ceil(filteredData.length / itemsPerPage);
    const startIndex = (currentPage - 1) * itemsPerPage;
    const currentData = filteredData.slice(startIndex, startIndex + itemsPerPage);

    // Reset pagination when filters change
    useEffect(() => {
        setCurrentPage(1);
    }, [searchNcp, trimestre, annee]);

    // Pagination logic for ellipsis
    const renderPaginationButtons = () => {
        const buttons = [];

        if (totalPages <= 5) {
            // Show all pages if 5 or fewer
            for (let i = 1; i <= totalPages; i++) {
                buttons.push(
                    <button
                        key={i}
                        onClick={() => setCurrentPage(i)}
                        className={`px-4 py-2 rounded-lg transition-all duration-200 font-medium ${
                            currentPage === i
                                ? 'bg-[#581D74] text-white shadow-lg transform scale-105'
                                : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50 hover:border-gray-400'
                        }`}
                    >
                        {i}
                    </button>
                );
            }
        } else {
            // Show 1, 2, 3, ..., last when more than 5 pages
            buttons.push(
                <button
                    key={1}
                    onClick={() => setCurrentPage(1)}
                    className={`px-4 py-2 rounded-lg transition-all duration-200 font-medium ${
                        currentPage === 1
                            ? 'bg-[#581D74] text-white shadow-lg transform scale-105'
                            : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50 hover:border-gray-400'
                    }`}
                >
                    1
                </button>
            );

            if (currentPage > 4) {
                buttons.push(
                    <span key="ellipsis1" className="px-2 py-2 text-gray-500">
                        ...
                    </span>
                );
            }

            // Show current page and neighbors
            const start = Math.max(2, currentPage - 1);
            const end = Math.min(totalPages - 1, currentPage + 1);

            for (let i = start; i <= end; i++) {
                if (i !== 1 && i !== totalPages) {
                    buttons.push(
                        <button
                            key={i}
                            onClick={() => setCurrentPage(i)}
                            className={`px-4 py-2 rounded-lg transition-all duration-200 font-medium ${
                                currentPage === i
                                    ? 'bg-[#581D74] text-white shadow-lg transform scale-105'
                                    : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50 hover:border-gray-400'
                            }`}
                        >
                            {i}
                        </button>
                    );
                }
            }

            if (currentPage < totalPages - 3) {
                buttons.push(
                    <span key="ellipsis2" className="px-2 py-2 text-gray-500">
                        ...
                    </span>
                );
            }

            if (totalPages > 1) {
                buttons.push(
                    <button
                        key={totalPages}
                        onClick={() => setCurrentPage(totalPages)}
                        className={`px-4 py-2 rounded-lg transition-all duration-200 font-medium ${
                            currentPage === totalPages
                                ? 'bg-[#581D74] text-white shadow-lg transform scale-105'
                                : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50 hover:border-gray-400'
                        }`}
                    >
                        {totalPages}
                    </button>
                );
            }
        }

        return buttons;
    };

    const formatCurrency = (value: number) => {
        return new Intl.NumberFormat('fr-FR', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }).format(value);
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 flex">
            <Sidebar currentPage="HISTORIQUE" />
            <div className="flex-1 ml-64 transition-all duration-300">
                <Header title="HISTORIQUE DES ARRÊTÉS TRIMESTRIELS" onBack={handleBack} />
                <main className="p-8">
                    <div className="max-w-7xl mx-auto space-y-8">

                        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-6">
                            <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4">
                                {/* Filtres à gauche */}
                                <form className="flex flex-col items-start gap-2" onSubmit={handleSearch}>
                                    <div className="flex gap-2">
                                        <select
                                            value={trimestre}
                                            onChange={(e) => setTrimestre(e.target.value as Trimestre)}
                                            className="px-4 py-3 w-20 h-12 border border-gray-300 rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 font-medium"
                                        >
                                            {trimestres.map((t) => (
                                                <option key={t.value} value={t.value}>
                                                    {t.label}
                                                </option>
                                            ))}
                                        </select>
                                        <input
                                            type="number"
                                            min="2000"
                                            max="2100"
                                            value={annee}
                                            onChange={(e) => setAnnee(Number(e.target.value))}
                                            className="px-4 py-3 h-12 border border-gray-300 rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 font-medium w-24"
                                        />
                                        <button
                                            type="submit"
                                            disabled={loading}
                                            className={`flex items-center px-6 py-3 bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white rounded-xl hover:from-[#4A1562] hover:to-[#581D74] transition-all duration-200 font-semibold shadow-lg hover:shadow-xl transform hover:scale-105 ${
                                                loading ? 'opacity-50 cursor-not-allowed' : ''
                                            }`}
                                        >
                                            {loading ? (
                                                <>
                                                    <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                                                    Chargement...
                                                </>
                                            ) : (
                                                'Afficher'
                                            )}
                                        </button>
                                    </div>
                                    <span className="text-sm text-[#581D74] font-semibold mt-1 pt-2">
                                        Période sélectionnée: {trimestre} {annee} ({periode.debut} au {periode.fin})
                                    </span>
                                </form>

                                {/* Barre de recherche à droite */}
                                <div className="relative">
                                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                                    <input
                                        type="text"
                                        placeholder="Rechercher par compte ou nom..."
                                        value={searchNcp}
                                        onChange={e => setSearchNcp(e.target.value)}
                                        className="pl-10 pr-4 py-3 w-80 h-11 border border-gray-300 rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200"
                                    />
                                </div>
                            </div>
                        </div>


                        {/* Error Message */}
                        {error && (
                            <div className="bg-red-50 border border-red-200 rounded-xl p-4">
                                <div className="flex items-center">
                                    <div className="flex-shrink-0">
                                        <div className="w-8 h-8 bg-red-100 rounded-full flex items-center justify-center">
                                            <span className="text-red-600 font-bold">!</span>
                                        </div>
                                    </div>
                                    <div className="ml-3">
                                        <p className="text-red-800 font-medium">{error}</p>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* Main Table */}
                        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
                            <div className="p-8 border-b border-gray-200">
                                <div className="flex items-center justify-between mb-6">
                                    <div>
                                        <h2 className="text-2xl font-bold text-gray-900">Arrêtés Trimestriels</h2>
                                        <p className="text-gray-600 mt-1">Historique des arrêtés pour la période sélectionnée</p>
                                    </div>
                                </div>

                                <div className="overflow-x-auto">
                                    <table className="min-w-full text-[15px]">
                                        <thead>
                                        <tr>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-6 py-4 text-left font-semibold rounded-l-xl whitespace-nowrap">
                                                Numéro de compte
                                            </th>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-6 py-4 text-left font-semibold whitespace-nowrap">
                                                Nom
                                            </th>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-6 py-4 text-left font-semibold whitespace-nowrap">
                                                Devise
                                            </th>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-6 py-4 text-right font-semibold whitespace-nowrap">
                                                Solde final
                                            </th>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-6 py-4 text-right font-semibold whitespace-nowrap">
                                                Net Agios
                                            </th>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-6 py-4 text-center font-semibold rounded-r-xl whitespace-nowrap">
                                                Actions
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {loading ? (
                                            <tr>
                                                <td colSpan={6} className="text-center py-12">
                                                    <div className="flex items-center justify-center space-x-2">
                                                        <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-[#581D74]"></div>
                                                        <span className="text-gray-600">Chargement des données...</span>
                                                    </div>
                                                </td>
                                            </tr>
                                        ) : currentData.length === 0 ? (
                                            <tr>
                                                <td colSpan={6} className="text-center py-12">
                                                    <div className="flex flex-col items-center">
                                                        <FileText className="w-12 h-12 text-gray-400 mb-4" />
                                                        <p className="text-gray-600 font-medium">Aucun arrêté trouvé</p>
                                                        <p className="text-gray-400 text-sm">Essayez de modifier vos critères de recherche</p>
                                                    </div>
                                                </td>
                                            </tr>
                                        ) : (
                                            currentData.map((item: HistoriqueArrete, index: number) => (
                                                <tr
                                                    key={index}
                                                    className={`hover:bg-gray-50 transition-colors duration-200 ${
                                                        index % 2 === 0 ? 'bg-white' : 'bg-gray-50/50'
                                                    }`}
                                                >
                                                    <td className="px-6 py-4 border-b border-gray-100">
                                                        <div className="flex items-center">
                                                            <span className="font-semibold text-gray-900">{item.ncp}</span>
                                                        </div>
                                                    </td>
                                                    <td className="px-6 py-4 border-b border-gray-100">
                                                        <span className="text-gray-900 font-medium">{item.nomrest}</span>
                                                    </td>
                                                    <td className="px-6 py-4 border-b border-gray-100">
                                                            <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800">
                                                                {item.dev}
                                                            </span>
                                                    </td>
                                                    <td className="px-6 py-4 border-b border-gray-100 text-right">
                                                            <span className={`font-bold ${item.soldeFinalCalcule >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                                                                {formatCurrency(item.soldeFinalCalcule)}
                                                            </span>
                                                    </td>
                                                    <td className="px-6 py-4 border-b border-gray-100 text-right">
                                                            <span className="font-bold text-gray-900">
                                                                {formatCurrency(item.netAgios)}
                                                            </span>
                                                    </td>
                                                    <td className="px-6 py-4 border-b border-gray-100">
                                                        <div className="flex justify-center">
                                                            <button
                                                                onClick={() => navigate(`/arretes/historique/detail?ncp=${item.ncp}&trimestre=${trimestre}&annee=${annee}`)}
                                                                className="p-2 text-[#581D74] hover:bg-purple-50 rounded-lg transition-colors duration-200 hover:scale-105 transform"
                                                                title="Voir les détails"
                                                            >
                                                                <BookOpen className="w-5 h-5" />
                                                            </button>
                                                        </div>
                                                    </td>
                                                </tr>
                                            ))
                                        )}
                                        </tbody>
                                    </table>
                                </div>

                                {/* Pagination */}
                                {totalPages > 1 && (
                                    <div className="flex items-center justify-between mt-8">
                                        <p className="text-sm text-gray-700">
                                            Affichage de <span className="font-medium">{startIndex + 1}</span> à{' '}
                                            <span className="font-medium">{Math.min(startIndex + itemsPerPage, filteredData.length)}</span> sur{' '}
                                            <span className="font-medium">{filteredData.length}</span> arrêtés
                                        </p>
                                        <div className="flex space-x-2">
                                            {/* Previous button */}
                                            <button
                                                onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
                                                disabled={currentPage === 1}
                                                className={`px-3 py-2 rounded-lg transition-all duration-200 font-medium ${
                                                    currentPage === 1
                                                        ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                                                        : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50 hover:border-gray-400'
                                                }`}
                                            >
                                                <ChevronLeft className="w-4 h-4" />
                                            </button>

                                            {/* Page numbers */}
                                            {renderPaginationButtons()}

                                            {/* Next button */}
                                            <button
                                                onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
                                                disabled={currentPage === totalPages}
                                                className={`px-3 py-2 rounded-lg transition-all duration-200 font-medium ${
                                                    currentPage === totalPages
                                                        ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                                                        : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50 hover:border-gray-400'
                                                }`}
                                            >
                                                <ChevronRight className="w-4 h-4" />
                                            </button>
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </main>

                <Footer/>
            </div>
        </div>
    );
};

export default HistoriqueArretes;