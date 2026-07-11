import { Role } from "./role.interface";


export interface Menu {
    id: string;
    code: string;
    name: string;
    icon?: string;
    route?: string;
    menuOrder?: string;
    category?: string;
    parent?: Menu | null;
    roles: Role[];
    children?: Menu[];
    mostrarSubMenu?: boolean;
}