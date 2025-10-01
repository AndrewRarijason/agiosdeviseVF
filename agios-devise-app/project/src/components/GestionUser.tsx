import React, { useEffect, useState } from 'react';
import Sidebar from './Sidebar';
import { CreditCard as Edit, Trash2, ChevronDown, Plus, Search, Users, UserCheck } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { Eye, EyeOff } from 'lucide-react';
import {
    fetchUsers,
    addUser,
    deleteUser,
    fetchRoles,
    User as ApiUser,
    Role
} from '../services/userService';
import Header from "./Header";

const GestionUser: React.FC = () => {
    const [users, setUsers] = useState<ApiUser[]>([]);
    const [roles, setRoles] = useState<Role[]>([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [filterOpen, setFilterOpen] = useState(false);
    const [selectedFilter, setSelectedFilter] = useState('Tous');
    const [searchTerm, setSearchTerm] = useState('');
    const [newUser, setNewUser] = useState({
        username: '',
        email_bmoi: '',
        mdp: '',
        role: 1,
        is_active: 1
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [fieldErrors, setFieldErrors] = useState<{ [key: string]: string }>({});
    const navigate = useNavigate();
    const handleBack = () => {
        navigate('/accueil');
    };
    const [showPassword, setShowPassword] = useState(false);
    const [isAddingUser, setIsAddingUser] = useState(false);

    useEffect(() => {
        fetchRoles()
            .then(setRoles)
            .catch(() => setError('Erreur lors du chargement des rôles'));
    }, []);

    useEffect(() => {
        setLoading(true);
        fetchUsers()
            .then(setUsers)
            .catch(() => setError('Erreur lors du chargement des utilisateurs'))
            .finally(() => setLoading(false));
    }, []);

    // Filtrage et recherche
    const filterOptions = ['Tous', ...roles.map((r: Role) => r.name)];
    const filteredUsers = users.filter(user => {
        const matchesFilter = selectedFilter === 'Tous' || user.role === selectedFilter;
        const matchesSearch = user.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
            user.email_bmoi.toLowerCase().includes(searchTerm.toLowerCase());
        return matchesFilter && matchesSearch;
    });

    // Pagination
    const usersPerPage = 5;
    const totalPages = Math.ceil(filteredUsers.length / usersPerPage);
    const startIndex = (currentPage - 1) * usersPerPage;
    const currentUsers = filteredUsers.slice(startIndex, startIndex + usersPerPage);

    // Reset pagination when filters change
    useEffect(() => {
        setCurrentPage(1);
    }, [selectedFilter, searchTerm]);

    // Suppression
    const handleDeleteUser = async (id: number) => {
        if (window.confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
            try {
                await deleteUser(id);
                setUsers(users.filter(u => u.id !== id));
            } catch {
                setError('Erreur lors de la suppression');
            }
        }
    };

    // Edition
    const handleEditUser = (id: number) => {
        navigate(`/users/edit/${id}`);
    };

    // Ajout
    const handleAddUser = async () => {
        if (!newUser.username || !newUser.email_bmoi || !newUser.mdp || !newUser.role) {
            setError('Veuillez remplir tous les champs obligatoires');
            return;
        }

        if (Object.values(fieldErrors).some(error => error)) {
            setError('Veuillez corriger les erreurs dans le formulaire');
            return;
        }

        setIsAddingUser(true);
        try {
            const created = await addUser({
                username: newUser.username,
                email_bmoi: newUser.email_bmoi,
                mdp: newUser.mdp,
                role: Number(newUser.role),
                is_active: newUser.is_active
            });
            setUsers([...users, created]);
            setNewUser({ username: '', email_bmoi: '', mdp: '', role: 1, is_active: 1 });
            setError(null);
            setFieldErrors({});
        } catch {
            setError('Erreur lors de l\'ajout de l\'utilisateur');
        } finally {
            setIsAddingUser(false);
        }
    };

    // Validation champ
    const validateField = async (name: string, value: string | number) => {
        let error = '';
        if (name === 'username') {
            if (!value) error = "Le nom d'utilisateur est requis";
            else if (users.some(u => u.username === value)) error = "Ce nom d'utilisateur est déjà utilisé";
        }
        if (name === 'email_bmoi') {
            if (!value) error = "L'email est requis";
            else if (typeof value === 'string' && !value.endsWith('@bmoi.mg')) error = "L'adresse email doit se terminer par @bmoi.mg";
        }
        if (name === 'mdp') {
            if (!value || (typeof value === 'string' && value.trim() === '')) error = "Le mot de passe ne peut pas être vide";
        }
        if (name === 'role') {
            if (!value) error = "Le rôle est requis";
        }
        setFieldErrors(prev => ({ ...prev, [name]: error }));
    };

    const getStatusBadge = (isActive: number) => {
        return isActive ? (
            <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                <UserCheck className="w-3 h-3 mr-1" />
                Actif
            </span>
        ) : (
            <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
                Inactif
            </span>
        );
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 flex">
            <Sidebar currentPage="Gestion utilisateurs" />
            <div className="flex-1 ml-64 transition-all duration-300">
                <Header title="GESTION DES UTILISATEURS" onBack={handleBack} />
                <main className="p-8">
                    <div className="max-w-7xl mx-auto space-y-8">
                        {/* Stats Cards */}
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                            <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-6 hover:shadow-lg transition-all duration-300">
                                <div className="flex items-center">
                                    <div className="p-3 bg-blue-100 rounded-xl">
                                        <Users className="w-6 h-6 text-blue-600" />
                                    </div>
                                    <div className="ml-4">
                                        <p className="text-sm font-medium text-gray-600">Total Utilisateurs</p>
                                        <p className="text-2xl font-bold text-gray-900">{users.length}</p>
                                    </div>
                                </div>
                            </div>
                            <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-6 hover:shadow-lg transition-all duration-300">
                                <div className="flex items-center">
                                    <div className="p-3 bg-green-100 rounded-xl">
                                        <UserCheck className="w-6 h-6 text-green-600" />
                                    </div>
                                    <div className="ml-4">
                                        <p className="text-sm font-medium text-gray-600">Utilisateurs Actifs</p>
                                        <p className="text-2xl font-bold text-gray-900">{users.filter(u => u.is_active).length}</p>
                                    </div>
                                </div>
                            </div>
                            <div className="bg-white rounded-2xl shadow-sm border border-gray-200 p-6 hover:shadow-lg transition-all duration-300">
                                <div className="flex items-center">
                                    <div className="p-3 bg-purple-100 rounded-xl">
                                        <Edit className="w-6 h-6 text-purple-600" />
                                    </div>
                                    <div className="ml-4">
                                        <p className="text-sm font-medium text-gray-600">Rôles</p>
                                        <p className="text-2xl font-bold text-gray-900">{roles.length}</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* Users Table */}
                        <div className="bg-white rounded-2xl shadow-sm border border-gray-200 overflow-hidden">
                            <div className="p-8 border-b border-gray-200">
                                <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-4 mb-6">
                                    <div>
                                        <h2 className="text-2xl font-bold text-gray-900">Utilisateurs</h2>
                                        <p className="text-gray-600 mt-1">Gérez tous vos utilisateurs en un seul endroit</p>
                                    </div>
                                    <div className="flex flex-col sm:flex-row gap-4">
                                        {/* Search */}
                                        <div className="relative">
                                            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                                            <input
                                                type="text"
                                                placeholder="Rechercher un utilisateur..."
                                                value={searchTerm}
                                                onChange={(e) => setSearchTerm(e.target.value)}
                                                className="pl-10 pr-4 py-3 w-full sm:w-80 border border-gray-300 rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200"
                                            />
                                        </div>
                                        {/* Filter */}
                                        <div className="relative">
                                            <button
                                                onClick={() => setFilterOpen(!filterOpen)}
                                                className="flex items-center px-6 py-3 bg-white border border-gray-300 rounded-xl hover:bg-gray-50 transition-all duration-200 font-medium"
                                            >
                                                <span className="mr-2">Filtre: {selectedFilter}</span>
                                                <ChevronDown className={`w-4 h-4 transition-transform duration-200 ${filterOpen ? 'rotate-180' : ''}`} />
                                            </button>
                                            {filterOpen && (
                                                <div className="absolute right-0 mt-2 w-56 bg-white border border-gray-200 rounded-xl shadow-lg z-20 overflow-hidden">
                                                    {filterOptions.map((option) => (
                                                        <button
                                                            key={option}
                                                            onClick={() => {
                                                                setSelectedFilter(option);
                                                                setFilterOpen(false);
                                                            }}
                                                            className={`w-full text-left px-4 py-3 hover:bg-gray-50 transition-colors duration-200 ${
                                                                selectedFilter === option ? 'bg-[#581D74] text-white hover:bg-[#581D74]' : ''
                                                            }`}
                                                        >
                                                            {option}
                                                        </button>
                                                    ))}
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                </div>

                                {error && (
                                    <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-xl">
                                        <p className="text-red-800 font-medium">{error}</p>
                                    </div>
                                )}

                                <div className="overflow-x-auto">
                                    <table className="w-full">
                                        <thead>
                                        <tr>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-6 py-4 text-left font-semibold rounded-l-xl">
                                                Nom d'utilisateur
                                            </th>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-6 py-4 text-left font-semibold">
                                                Email
                                            </th>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-6 py-4 text-left font-semibold">
                                                Rôle
                                            </th>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-6 py-4 text-left font-semibold">
                                                Statut
                                            </th>
                                            <th className="bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white px-6 py-4 text-center font-semibold rounded-r-xl">
                                                Actions
                                            </th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        {loading ? (
                                            <tr>
                                                <td colSpan={5} className="text-center py-12">
                                                    <div className="flex items-center justify-center space-x-2">
                                                        <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-[#581D74]"></div>
                                                        <span className="text-gray-600">Chargement...</span>
                                                    </div>
                                                </td>
                                            </tr>
                                        ) : currentUsers.length === 0 ? (
                                            <tr>
                                                <td colSpan={5} className="text-center py-12">
                                                    <div className="flex flex-col items-center">
                                                        <Users className="w-12 h-12 text-gray-400 mb-4" />
                                                        <p className="text-gray-600 font-medium">Aucun utilisateur trouvé</p>
                                                        <p className="text-gray-400 text-sm">Essayez de modifier vos filtres de recherche</p>
                                                    </div>
                                                </td>
                                            </tr>
                                        ) : currentUsers.map((user, index) => (
                                            <tr key={user.id} className={`hover:bg-gray-50 transition-colors duration-200 ${index % 2 === 0 ? 'bg-white' : 'bg-gray-50/50'}`}>
                                                <td className="px-6 py-4 border-b border-gray-100">
                                                    <div className="font-semibold text-gray-900">{user.username}</div>
                                                </td>
                                                <td className="px-6 py-4 border-b border-gray-100">
                                                    <span className="text-gray-600">{user.email_bmoi}</span>
                                                </td>
                                                <td className="px-6 py-4 border-b border-gray-100">
                                                    <span className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-blue-100 text-blue-800">
                                                        {user.role}
                                                    </span>
                                                </td>
                                                <td className="px-6 py-4 border-b border-gray-100">
                                                    {getStatusBadge(user.is_active)}
                                                </td>
                                                <td className="px-6 py-4 border-b border-gray-100">
                                                    <div className="flex justify-center space-x-2">
                                                        <button
                                                            onClick={() => handleEditUser(user.id)}
                                                            className="p-2 text-blue-600 hover:bg-blue-50 rounded-lg transition-colors duration-200 hover:scale-105 transform"
                                                            title="Modifier"
                                                        >
                                                            <Edit className="w-5 h-5" />
                                                        </button>
                                                        <button
                                                            onClick={() => handleDeleteUser(user.id)}
                                                            className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors duration-200 hover:scale-105 transform"
                                                            title="Supprimer"
                                                        >
                                                            <Trash2 className="w-5 h-5" />
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                </div>

                                {/* Pagination */}
                                {totalPages > 1 && (
                                    <div className="flex items-center justify-between mt-8">
                                        <p className="text-sm text-gray-700">
                                            Affichage de <span className="font-medium">{startIndex + 1}</span> à{' '}
                                            <span className="font-medium">{Math.min(startIndex + usersPerPage, filteredUsers.length)}</span> sur{' '}
                                            <span className="font-medium">{filteredUsers.length}</span> utilisateurs
                                        </p>
                                        <div className="flex space-x-2">
                                            {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
                                                <button
                                                    key={page}
                                                    onClick={() => setCurrentPage(page)}
                                                    className={`px-4 py-2 rounded-lg transition-all duration-200 font-medium ${
                                                        currentPage === page
                                                            ? 'bg-[#581D74] text-white shadow-lg transform scale-105'
                                                            : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50 hover:border-gray-400'
                                                    }`}
                                                >
                                                    {page}
                                                </button>
                                            ))}
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>

                        {/* Add User Form */}
                        <div className="bg-white rounded-2xl shadow-sm border border-gray-200">
                            <div className="p-8">
                                <div className="flex items-center mb-8">
                                    <div className="p-3 bg-[#581D74] rounded-xl">
                                        <Plus className="w-6 h-6 text-white" />
                                    </div>
                                    <div className="ml-4">
                                        <h2 className="text-2xl font-bold text-gray-900">Ajouter un nouvel utilisateur</h2>
                                        <p className="text-gray-600 mt-1">Créez un nouveau compte utilisateur</p>
                                    </div>
                                </div>

                                <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                                    <div className="space-y-2">
                                        <label className="block text-sm font-semibold text-gray-700">Nom d'utilisateur</label>
                                        <input
                                            type="text"
                                            placeholder="Saisir le nom d'utilisateur"
                                            value={newUser.username}
                                            onChange={e => setNewUser({ ...newUser, username: e.target.value })}
                                            onBlur={e => validateField('username', e.target.value)}
                                            className={`w-full px-4 py-3 border rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 ${
                                                fieldErrors.username ? 'border-red-300 bg-red-50' : 'border-gray-300'
                                            }`}
                                        />
                                        {fieldErrors.username && (
                                            <p className="text-red-600 text-sm mt-1 font-medium">{fieldErrors.username}</p>
                                        )}
                                    </div>

                                    <div className="space-y-2">
                                        <label className="block text-sm font-semibold text-gray-700">Mot de passe</label>
                                        <div className="relative">
                                            <input
                                                type={showPassword ? "text" : "password"}
                                                placeholder="Mot de passe"
                                                value={newUser.mdp}
                                                onChange={e => setNewUser({ ...newUser, mdp: e.target.value })}
                                                onBlur={e => validateField('mdp', e.target.value)}
                                                className={`w-full px-4 py-3 pr-12 border rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 ${
                                                    fieldErrors.mdp ? 'border-red-300 bg-red-50' : 'border-gray-300'
                                                }`}
                                            />
                                            <button
                                                type="button"
                                                onClick={() => setShowPassword(!showPassword)}
                                                className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors duration-200"
                                                tabIndex={-1}
                                            >
                                                {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
                                            </button>
                                        </div>
                                        {fieldErrors.mdp && (
                                            <p className="text-red-600 text-sm mt-1 font-medium">{fieldErrors.mdp}</p>
                                        )}
                                    </div>

                                    <div className="space-y-2">
                                        <label className="block text-sm font-semibold text-gray-700">Email</label>
                                        <input
                                            type="email"
                                            placeholder="exemple@bmoi.mg"
                                            value={newUser.email_bmoi}
                                            onChange={e => setNewUser({ ...newUser, email_bmoi: e.target.value })}
                                            onBlur={e => validateField('email_bmoi', e.target.value)}
                                            className={`w-full px-4 py-3 border rounded-xl focus:ring-2 focus:ring-[#581D74] focus:border-transparent outline-none transition-all duration-200 ${
                                                fieldErrors.email_bmoi ? 'border-red-300 bg-red-50' : 'border-gray-300'
                                            }`}
                                        />
                                        {fieldErrors.email_bmoi && (
                                            <p className="text-red-600 text-sm mt-1 font-medium">{fieldErrors.email_bmoi}</p>
                                        )}
                                    </div>

                                    <div className="space-y-2">
                                        <label className="block text-sm font-semibold text-gray-700">Rôle</label>
                                        <select
                                            value={newUser.role}
                                            onChange={e => setNewUser({ ...newUser, role: Number(e.target.value) })}
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
                                </div>

                                <div className="flex justify-center">
                                    <button
                                        onClick={handleAddUser}
                                        disabled={isAddingUser}
                                        className={`flex items-center px-8 py-4 bg-gradient-to-r from-[#581D74] to-[#6B2C87] text-white rounded-xl hover:from-[#4A1562] hover:to-[#581D74] transition-all duration-200 font-semibold shadow-lg hover:shadow-xl transform hover:scale-105 ${
                                            isAddingUser ? 'opacity-50 cursor-not-allowed' : ''
                                        }`}
                                    >
                                        {isAddingUser ? (
                                            <>
                                                <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                                                Ajout en cours...
                                            </>
                                        ) : (
                                            <>
                                                <Plus className="w-5 h-5 mr-2" />
                                                AJOUTER L'UTILISATEUR
                                            </>
                                        )}
                                    </button>
                                </div>
                            </div>
                        </div>
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

export default GestionUser;