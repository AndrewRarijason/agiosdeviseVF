import React, { useEffect, useState } from 'react';
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { getRepartitionAgios } from '../services/dashboardArreteService';
import { TrendingUp, AlertCircle, Loader2 } from 'lucide-react';

interface RepartitionAgiosData {
    dev: string;
    dateDebutArrete: string;
    libele: string;
    cdAlpha: string;
    dateFinArrete: string;
    totalNetAgios: number;
}

interface Props {
    dateDebutArrete: string;
    dateFinArrete: string;
}

const COLORS = [
    '#EF4444', // Red
    '#3B82F6', // Blue
    '#F59E0B', // Amber
    '#10B981', // Green
    '#EF4444', // Red
    '#06B6D4'  // Cyan
];

const CustomTooltip = ({ active, payload }: { active?: boolean; payload?: any[] }) => {
    if (active && payload && payload.length) {
        const { cdAlpha, totalNetAgios, libele } = payload[0].payload as RepartitionAgiosData;
        return (
            <div className="bg-white p-4 rounded-xl shadow-lg border border-gray-200">
                <div className="space-y-2">
                    <p className="font-semibold text-gray-900">{libele}</p>
                    <p className="text-sm text-gray-600">Code: {cdAlpha}</p>
                    <p className="text-lg font-bold text-[#581D74]">
                        {totalNetAgios.toLocaleString('fr-FR', {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2
                        })}
                    </p>
                </div>
            </div>
        );
    }
    return null;
};

const CustomLegend = ({ payload }: { payload?: any[] }) => {
    if (!payload) return null;

    return (
        <div className="flex flex-wrap justify-center gap-4 mt-6">
            {payload.map((entry, index) => (
                <div key={index} className="flex items-center space-x-2">
                    <div
                        className="w-4 h-4 rounded-full shadow-sm"
                        style={{ backgroundColor: entry.color }}
                    />
                    <span className="text-sm font-medium text-gray-700">
                        {entry.value}
                    </span>
                </div>
            ))}
        </div>
    );
};

const RepartitionAgios: React.FC<Props> = ({ dateDebutArrete, dateFinArrete }) => {
    const [data, setData] = useState<RepartitionAgiosData[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (dateDebutArrete && dateFinArrete) {
            setLoading(true);
            setError(null);
            getRepartitionAgios(dateDebutArrete, dateFinArrete)
                .then(setData)
                .catch(e => setError(e.message || 'Erreur chargement agios'))
                .finally(() => setLoading(false));
        }
    }, [dateDebutArrete, dateFinArrete]);

    if (!dateDebutArrete || !dateFinArrete) return null;

    const formatDate = (dateStr: string) => {
        return new Date(dateStr).toLocaleDateString('fr-FR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    };

    const totalAgios = data.reduce((sum, item) => sum + item.totalNetAgios, 0);

    return (
        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
            <div className="p-8">
                {/* Header */}
                <div className="flex items-center justify-between mb-8">
                    <div className="flex items-center space-x-3">
                        <div className="p-3 bg-gradient-to-br from-[#581D74] to-[#6B2C87] rounded-xl">
                            <TrendingUp className="w-6 h-6 text-white" />
                        </div>
                        <div>
                            <h2 className="text-2xl font-bold text-gray-900">Répartition top 3 des Agios par Devise</h2>
                            <p className="text-gray-600 mt-1">
                                Période: {formatDate(dateDebutArrete)} - {formatDate(dateFinArrete)}
                            </p>
                        </div>
                    </div>
                    {/* {totalAgios > 0 && (
                        <div className="text-right">
                            <p className="text-sm font-medium text-gray-600">Total Agios</p>
                            <p className="text-2xl font-bold text-[#581D74]">
                                {totalAgios.toLocaleString('fr-FR', {
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2
                                })}
                            </p>
                        </div>
                    )}
                    */}
                </div>

                {/* Content */}
                <div className="relative">
                    {loading && (
                        <div className="flex items-center justify-center py-16">
                            <div className="flex items-center space-x-3">
                                <Loader2 className="w-6 h-6 text-[#581D74] animate-spin" />
                                <span className="text-gray-600 font-medium">Chargement des données...</span>
                            </div>
                        </div>
                    )}

                    {error && (
                        <div className="flex items-center justify-center py-16">
                            <div className="flex flex-col items-center space-y-4">
                                <div className="p-3 bg-red-100 rounded-full">
                                    <AlertCircle className="w-8 h-8 text-red-600" />
                                </div>
                                <div className="text-center">
                                    <p className="text-red-800 font-semibold">Erreur de chargement</p>
                                    <p className="text-red-600 text-sm mt-1">{error}</p>
                                </div>
                            </div>
                        </div>
                    )}

                    {!loading && !error && data.length === 0 && (
                        <div className="flex items-center justify-center py-16">
                            <div className="flex flex-col items-center space-y-4">
                                <div className="p-3 bg-gray-100 rounded-full">
                                    <TrendingUp className="w-8 h-8 text-gray-400" />
                                </div>
                                <div className="text-center">
                                    <p className="text-gray-600 font-semibold">Aucune donnée disponible</p>
                                    <p className="text-gray-500 text-sm mt-1">
                                        Aucun agio trouvé pour la période sélectionnée
                                    </p>
                                </div>
                            </div>
                        </div>
                    )}

                    {!loading && !error && data.length > 0 && (
                        <div className="space-y-6">
                            {/* Chart */}
                            <div className="bg-gradient-to-br from-gray-50 to-white rounded-xl p-6 border border-gray-100">
                                <ResponsiveContainer width="100%" height={400}>
                                    <PieChart>
                                        <Pie
                                            data={data}
                                            dataKey="totalNetAgios"
                                            nameKey="libele"
                                            cx="50%"
                                            cy="50%"
                                            outerRadius={120}
                                            innerRadius={40}
                                            paddingAngle={2}
                                            label={({ libele, percent }) =>
                                                `${libele} (${percent ? (percent * 100).toFixed(1) : '0'}%)`
                                            }
                                            labelLine={false}
                                        >
                                            {data.map((_, index) => (
                                                <Cell
                                                    key={`cell-${index}`}
                                                    fill={COLORS[index % COLORS.length]}
                                                    stroke="#fff"
                                                    strokeWidth={2}
                                                />
                                            ))}
                                        </Pie>
                                        <Tooltip content={<CustomTooltip />} />
                                        <Legend content={<CustomLegend />} />
                                    </PieChart>
                                </ResponsiveContainer>
                            </div>

                            {/* Data Table */}
                            <div className="bg-gray-50 rounded-xl p-6">
                                <h3 className="text-lg font-semibold text-gray-900 mb-4">Détail par Devise</h3>
                                <div className="overflow-x-auto">
                                    <table className="w-full">
                                        <thead>
                                        <tr>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-4 py-3 text-left font-semibold rounded-l-xl">
                                                Code
                                            </th>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-4 py-3 text-left font-semibold">
                                                Devise
                                            </th>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-4 py-3 text-right font-semibold">
                                                Montant Agios
                                            </th>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-4 py-3 text-right font-semibold rounded-r-xl">
                                                Pourcentage
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {data.map((item, index) => {
                                            const percentage = totalAgios > 0 ? (item.totalNetAgios / totalAgios) * 100 : 0;
                                            return (
                                                <tr
                                                    key={index}
                                                    className={`hover:bg-white transition-colors duration-200 ${
                                                        index % 2 === 0 ? 'bg-white' : 'bg-gray-50/50'
                                                    }`}
                                                >
                                                    <td className="px-4 py-3 border-b border-gray-100">
                                                        <div className="flex items-center">
                                                            <div
                                                                className="w-4 h-4 rounded-full mr-3"
                                                                style={{ backgroundColor: COLORS[index % COLORS.length] }}
                                                            />
                                                            <span className="font-semibold text-gray-900">{item.cdAlpha}</span>
                                                        </div>
                                                    </td>
                                                    <td className="px-4 py-3 border-b border-gray-100">
                                                        <span className="text-gray-900 font-medium">{item.libele}</span>
                                                    </td>
                                                    <td className="px-4 py-3 border-b border-gray-100 text-right">
                                                            <span className="font-bold text-gray-900">
                                                                {item.totalNetAgios.toLocaleString('fr-FR', {
                                                                    minimumFractionDigits: 2,
                                                                    maximumFractionDigits: 2
                                                                })}
                                                            </span>
                                                    </td>
                                                    <td className="px-4 py-3 border-b border-gray-100 text-right">
                                                            <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-bold bg-[#581D74] bg-opacity-10 text-[#581D74]">
                                                                {percentage.toFixed(1)}%
                                                            </span>
                                                    </td>
                                                </tr>
                                            );
                                        })}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default RepartitionAgios;