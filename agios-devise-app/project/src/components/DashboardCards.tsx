import React from 'react';
import { Calendar, Users, AlertCircle, Clock, CheckCircle, TrendingUp } from 'lucide-react';

interface DashboardCardsProps {
    periodeValidee: { debut: string; fin: string } | null;
    loading: boolean;
    error: string | null;
    data: any;
    formatNumber: (num: number) => string;
    formatPeriode: (dateDebut: string, dateFin: string) => string;
    formatDureeCalcul: (debut: string, fin: string) => string;
}

const DashboardCards: React.FC<DashboardCardsProps> = ({
                                                           periodeValidee,
                                                           loading,
                                                           error,
                                                           data,
                                                           formatNumber,
                                                           formatPeriode,
                                                           formatDureeCalcul,
                                                       }) => (
    <>
        {!periodeValidee && (
            <div className="flex items-center justify-center py-20">
                <div className="text-center">
                    <div className="p-4 bg-gray-100 rounded-full w-20 h-20 flex items-center justify-center mx-auto mb-6">
                        <Calendar className="w-10 h-10 text-gray-400" />
                    </div>
                    <h3 className="text-xl font-semibold text-gray-900 mb-2">Sélectionnez une période</h3>
                    <p className="text-gray-600 max-w-md">
                        Choisissez un trimestre et une année, puis validez pour afficher les données du tableau de bord
                    </p>
                </div>
            </div>
        )}

        {loading && (
            <div className="flex items-center justify-center py-20">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-4 border-gray-200 border-t-[#581D74] mx-auto mb-6"></div>
                    <h3 className="text-xl font-semibold text-gray-900 mb-2">Chargement en cours</h3>
                    <p className="text-gray-600">Récupération des données...</p>
                </div>
            </div>
        )}

        {error && (
            <div className="bg-red-50 border border-red-200 rounded-2xl p-8">
                <div className="flex items-center justify-center">
                    <div className="text-center">
                        <div className="p-3 bg-red-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                            <AlertCircle className="w-8 h-8 text-red-600" />
                        </div>
                        <h3 className="text-lg font-semibold text-red-800 mb-2">Erreur de chargement</h3>
                        <p className="text-red-700">{error}</p>
                    </div>
                </div>
            </div>
        )}

        {periodeValidee && !loading && !error && !data && (
            <div className="flex items-center justify-center py-20">
                <div className="text-center">
                    <div className="p-4 bg-yellow-100 rounded-full w-20 h-20 flex items-center justify-center mx-auto mb-6">
                        <AlertCircle className="w-10 h-10 text-yellow-600" />
                    </div>
                    <h3 className="text-xl font-semibold text-gray-900 mb-2">Aucune donnée disponible</h3>
                    <p className="text-gray-600 max-w-md">
                        Aucune information trouvée pour la période sélectionnée
                    </p>
                </div>
            </div>
        )}

        {data && (
            <div className="space-y-8">
                {/* Header Section */}
                <div className="text-center">
                    <h2 className="text-3xl font-bold text-gray-900 mb-2">Résumé du Traitement</h2>
                    <p className="text-gray-600 text-lg">
                        {formatPeriode(data.dateDebuArrete, data.dateFinArrete)}
                    </p>
                </div>

                {/* Main Content Cards */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
                    {/* Processing Summary */}
                    <div className="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-2xl p-8 border border-blue-200">
                        <div className="flex items-center space-x-3 mb-6">
                            <div className="p-3 bg-blue-500 rounded-xl">
                                <Users className="w-6 h-6 text-white" />
                            </div>
                            <div>
                                <h3 className="text-xl font-bold text-gray-900">Comptes Traités</h3>
                                <p className="text-blue-600 font-medium">Résultats du processus</p>
                            </div>
                        </div>

                        <div className="space-y-4">
                            <div className="flex items-center justify-between p-4 bg-white rounded-xl border border-blue-100">
                                <span className="text-gray-700 font-medium">Total de comptes traités</span>
                                <span className="text-2xl font-bold text-blue-600">
                                    {formatNumber(data.nbTotalComptes)}
                                </span>
                            </div>

                            <div className="flex items-center justify-between p-4 bg-white rounded-xl border border-red-100">
                                <span className="text-gray-700 font-medium">Comptes KO</span>
                                <span className="text-2xl font-bold text-red-600">
                                    {formatNumber(data.nbCompteKo)}
                                </span>
                            </div>

                            <div className="flex items-center justify-between p-4 bg-white rounded-xl border border-green-100">
                                <span className="text-gray-700 font-medium">Comptes OK</span>
                                <span className="text-2xl font-bold text-green-600">
                                    {formatNumber(data.nbTotalComptes - data.nbCompteKo)}
                                </span>
                            </div>
                        </div>

                        {/* Success Rate Bar */}
                        <div className="mt-6 p-4 bg-white rounded-xl border border-blue-100">
                            <div className="flex items-center justify-between mb-3">
                                <span className="text-gray-700 font-medium">Taux de réussite</span>
                                <span className="text-lg font-bold text-gray-900">
                                    {((data.nbTotalComptes - data.nbCompteKo) / data.nbTotalComptes * 100).toFixed(1)}%
                                </span>
                            </div>
                            <div className="w-full bg-gray-200 rounded-full h-3">
                                <div
                                    className="bg-gradient-to-r from-green-500 to-green-600 h-3 rounded-full transition-all duration-500 shadow-sm"
                                    style={{
                                        width: `${((data.nbTotalComptes - data.nbCompteKo) / data.nbTotalComptes * 100)}%`
                                    }}
                                />
                            </div>
                        </div>
                    </div>

                    {/* Timing Information */}
                    <div className="bg-gradient-to-br from-purple-50 to-pink-50 rounded-2xl p-8 border border-purple-200">
                        <div className="flex items-center space-x-3 mb-6">
                            <div className="p-3 bg-purple-500 rounded-xl">
                                <Clock className="w-6 h-6 text-white" />
                            </div>
                            <div>
                                <h3 className="text-xl font-bold text-gray-900">Informations Temporelles</h3>
                                <p className="text-purple-600 font-medium">Durée et période</p>
                            </div>
                        </div>

                        <div className="space-y-6">
                            <div className="p-4 bg-white rounded-xl border border-purple-100">
                                <div className="flex items-center space-x-3 mb-2">
                                    <Calendar className="w-5 h-5 text-purple-500" />
                                    <span className="text-gray-700 font-medium">Période trimestrielle</span>
                                </div>
                                <p className="text-lg font-semibold text-gray-900 ml-8">
                                    {formatPeriode(data.dateDebuArrete, data.dateFinArrete)}
                                </p>
                            </div>

                            <div className="p-4 bg-white rounded-xl border border-purple-100">
                                <div className="flex items-center space-x-3 mb-2">
                                    <TrendingUp className="w-5 h-5 text-purple-500" />
                                    <span className="text-gray-700 font-medium">Durée de traitement</span>
                                </div>
                                <p className="text-lg font-semibold text-gray-900 ml-8">
                                    {formatDureeCalcul(data.dateHeureDebutCalcul, data.dateHeureFinCalcul)}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )}
    </>
);

export default DashboardCards;