import React, { useEffect, useState } from 'react';
import Sidebar from './Sidebar';
import { fetchRoles, updateUser, fetchUsers, User as ApiUser, Role } from '../services/userService';
import { useParams, useNavigate } from 'react-router-dom';
import Header from './Header';
import { User, Mail, Shield, Save, AlertCircle, Loader2 } from 'lucide-react';

const EditUser: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [roles, setRoles] = useState<Role[]>([]);
    const [user, setUser] = useState<ApiUser | null>(null);
    const [fieldErrors, setFieldErrors] = useState<{ [key: string]: string }>({});
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState(false);
    const navigate = useNavigate();

    const handleBack = () => {
        navigate('/gestion-utilisateurs');
    };

    useEffect(() => {
        const loadData = async () => {
            try {
                const [rolesData, usersData] = await Promise.all([
                    fetchRoles(),
                    fetchUsers()
                ]);
                setRoles(rolesData);
                const found = usersData.find(u => u.id === Number(id));
                if (found) {
                    setUser(found);
                } else {
                    setError("Utilisateur introuvable");
                }
            } catch (err) {
                setError("Erreur lors du chargement des données " + err);
            }
        };
        loadData();
    }, [id]);

    const validateField = (name: string, value: string | number) => {
        let error = '';
        if (name === 'username') {
            if (!value) error = "Le nom d'utilisateur est requis";
        }
        if (name === 'email_bmoi') {
            if (!value) error = "L'email est requis";
            else if (typeof value === 'string' && !value.endsWith('@bmoi.mg'))
                error = "L'adresse email doit se terminer par @bmoi.mg";
        }
        if (name === 'role') {
            if (!value) error = "Le rôle est requis";
        }
        setFieldErrors(prev => ({ ...prev, [name]: error }));
    };

    const handleUpdate = async () => {
        if (!user) return;

        // Validate all fields
        validateField('username', user.username);
        validateField('email_bmoi', user.email_bmoi);
        validateField('role', user.role);

        // Check if there are any errors
        if (Object.values(fieldErrors).some(error => error)) {
            setError('Veuillez corriger les erreurs dans le formulaire');
            return;
        }

        setLoading(true);
        setError(null);
        setSuccess(false);

        try {
            await updateUser(user.id, {
                username: user.username,
                email_bmoi: user.email_bmoi,
                role: typeof user.role === 'string' ?
                    roles.find(r => r.name === user.role)?.id || 1 :
                    user.role,
                is_active: user.is_active
            });
            setSuccess(true);
            setTimeout(() => {
                navigate('/gestion-utilisateurs');
            }, 1500);
        } catch (e: unknown) {
            if (e instanceof Error) {
                setError(e.message || "Erreur lors de la modification");
            } else {
                setError("Erreur lors de la modification");
            }
        } finally {
            setLoading(false);
        }
    };

    if (!user && !error) {
        return (
            <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 flex">
                <Sidebar currentPage="Gestion utilisateurs" />
                <div className="flex-1 ml-64 transition-all duration-300">
                    <Header title="MODIFIER UTILISATEUR" onBack={handleBack}/>
                    <main className="p-8">
                        <div className="flex items-center justify-center py-20">
                            <div className="text-center">
                                <div className="animate-spin rounded-full h-12 w-12 border-4 border-gray-200 border-t-[#581D74] mx-auto mb-6"></div>
                                <h3 className="text-xl font-semibold text-gray-900 mb-2">Chargement</h3>
                                <p className="text-gray-600">Récupération des données utilisateur...</p>
                            </div>
                        </div>
                    </main>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 flex">
            <Sidebar currentPage="Gestion utilisateurs" />
            <div className="flex-1 ml-64 transition-all duration-300">
                <Header title="MODIFIER UTILISATEUR" onBack={handleBack}/>
                <main className="p-8">
                    <div className="max-w-4xl mx-auto space-y-8">

                        {/* Error State */}
                        {error && !user && (
                            <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-8">
                                <div className="flex items-center justify-center py-12">
                                    <div className="text-center">
                                        <div className="p-3 bg-red-100 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                                            <AlertCircle className="w-8 h-8 text-red-600" />
                                        </div>
                                        <h3 className="text-lg font-semibold text-red-800 mb-2">Erreur</h3>
                                        <p className="text-red-700">{error}</p>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* Edit Form */}
                        {user && (
                            <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
                                <div className="p-8 border-b border-gray-200">
                                    <div className="flex items-center space-x-3 mb-8">
                                        <div className="p-3 bg-gradient-to-br from-[#581D74] to-[#6B2C87] rounded-xl">
                                            <User className="w-6 h-6 text-white" />
                                        </div>
                                        <div>
                                            <h2 className="text-2xl font-bold text-gray-900">Modification de l'utilisateur</h2>
                                            <p className="text-gray-600 mt-1">Modifiez les informations de l'utilisateur</p>
                                        </div>
                                    </div>

                                    {/* Success Message */}
                                    {success && (
                                        <div className="mb-6 p-4 bg-green-50 border border-green-200 rounded-xl">
                                            <div className="flex items-center">
                                                <p className="text-green-800 font-medium">Utilisateur modifié avec succès ! Redirection en cours...</p>
                                            </div>
                                        </div>
                                    )}

                                    {/* Error Message */}
                                    {error && user && (
                                        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-xl">
                                            <div className="flex items-center">
                                                <AlertCircle className="w-5 h-5 text-red-600 mr-2" />
                                                <p className="text-red-800 font-medium">{error}</p>
                                            </div>
                                        </div>
                                    )}

                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
                                        {/* Username Field */}
                                        <div className="space-y-2">
                                            <label className="block text-sm font-semibold text-gray-700">
                                                <User className="w-4 h-4 inline mr-2" />
                                                Nom d'utilisateur
                                            </label>
                                            <input
                                                type="text"
                                                placeholder="Saisir le nom d'utilisateur"
                                                value={user.username}
                                                onChange={e => setUser({ ...user, username: e.target.value })}
                                                onBlur={e => validateField('username', e.target.value)}
                                                className={`w-full px-4 py-3 border rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 ${
                                                    fieldErrors.username ? 'border-red-300 bg-red-50' : 'border-gray-300'
                                                }`}
                                            />
                                            {fieldErrors.username && (
                                                <p className="text-red-600 text-sm mt-1 font-medium">{fieldErrors.username}</p>
                                            )}
                                        </div>

                                        {/* Email Field */}
                                        <div className="space-y-2">
                                            <label className="block text-sm font-semibold text-gray-700">
                                                <Mail className="w-4 h-4 inline mr-2" />
                                                Email
                                            </label>
                                            <input
                                                type="email"
                                                placeholder="exemple@bmoi.mg"
                                                value={user.email_bmoi}
                                                onChange={e => setUser({ ...user, email_bmoi: e.target.value })}
                                                onBlur={e => validateField('email_bmoi', e.target.value)}
                                                className={`w-full px-4 py-3 border rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 ${
                                                    fieldErrors.email_bmoi ? 'border-red-300 bg-red-50' : 'border-gray-300'
                                                }`}
                                            />
                                            {fieldErrors.email_bmoi && (
                                                <p className="text-red-600 text-sm mt-1 font-medium">{fieldErrors.email_bmoi}</p>
                                            )}
                                        </div>

                                        {/* Role Field */}
                                        <div className="space-y-2">
                                            <label className="block text-sm font-semibold text-gray-700">
                                                <Shield className="w-4 h-4 inline mr-2" />
                                                Rôle
                                            </label>
                                            <select
                                                value={typeof user.role === 'string' ?
                                                    roles.find(r => r.name === user.role)?.id || '' :
                                                    user.role}
                                                onChange={e => setUser({ ...user, role: Number(e.target.value) })}
                                                onBlur={e => validateField('role', e.target.value)}
                                                className={`w-full px-4 py-3 border rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 ${
                                                    fieldErrors.role ? 'border-red-300 bg-red-50' : 'border-gray-300'
                                                }`}
                                            >
                                                <option value="">Sélectionner un rôle</option>
                                                {roles.map(role => (
                                                    <option key={role.id} value={role.id}>{role.name}</option>
                                                ))}
                                            </select>
                                            {fieldErrors.role && (
                                                <p className="text-red-600 text-sm mt-1 font-medium">{fieldErrors.role}</p>
                                            )}
                                        </div>

                                        {/* Status Field */}
                                        <div className="space-y-2">
                                            <label className="block text-sm font-semibold text-gray-700">Statut</label>
                                            <select
                                                value={user.is_active}
                                                onChange={e => setUser({ ...user, is_active: Number(e.target.value) })}
                                                className="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200"
                                            >
                                                <option value={1}>Actif</option>
                                                <option value={0}>Inactif</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div className="flex justify-center">
                                        <button
                                            onClick={handleUpdate}
                                            disabled={loading || success}
                                            className={`flex items-center px-12 py-4 bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white rounded-xl hover:from-[#4A1562] hover:to-[#581D74] transition-all duration-200 font-semibold shadow-lg hover:shadow-xl transform hover:scale-105 ${
                                                loading || success ? 'opacity-50 cursor-not-allowed' : ''
                                            }`}
                                        >
                                            {loading ? (
                                                <>
                                                    <Loader2 className="w-5 h-5 mr-2 animate-spin" />
                                                    ENREGISTREMENT...
                                                </>
                                            ) : success ? (
                                                <>
                                                    ENREGISTRÉ
                                                </>
                                            ) : (
                                                <>
                                                    <Save className="w-5 h-5 mr-2" />
                                                    ENREGISTRER LES MODIFICATIONS
                                                </>
                                            )}
                                        </button>
                                    </div>
                                </div>
                            </div>
                        )}
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
    );
};

export default EditUser;