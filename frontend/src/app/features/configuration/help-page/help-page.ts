import { Component, inject, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { BreadCrumb } from "../../../shared/ui/bread-crumb/bread-crumb";
import { PageHeader } from "../../../shared/ui/page-header/page-header";
import { BreadcrumbItem } from '../../../core/models/bread-crumb.interface';
import { firstValueFrom } from 'rxjs/internal/firstValueFrom';
import { AuthService } from '../../../core/services/auth.service';

interface IconExplanation {
  icon: string;
  label: string;
  description: string;
}

interface HelpSection {
  id: string;
  icon: string;
  title: string;
}
@Component({
  selector: 'app-help-page',
  standalone: true,
  imports: [MatIconModule, BreadCrumb, PageHeader],
  templateUrl: './help-page.html',
  styleUrl: './help-page.css',
})
export class HelpPage {
  private readonly authService = inject(AuthService)
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly icon = "help_outline";
  readonly title = "Guía del Sistema";
  readonly subtitle = "Aprende a utilizar los módulos y herramientas disponibles del sistema";
  async ngOnInit(): Promise<void> {

    const user = await firstValueFrom(this.authService.getCurrentUser());

    if (!user) return;

    const homeRoute = this.authService.getHomeByRole(user.role);

    this.breadcrumbs.set([
      { label: 'Inicio', href: homeRoute },
      { label: 'Gui de Sistema' }
    ]);

  }
  readonly activeSection = signal('intro');

  readonly sections: HelpSection[] = [
    { id: 'intro', icon: 'info', title: 'Introducción' },
    { id: 'breadcrumb', icon: 'route', title: 'Ruta de navegación' },
    { id: 'botones', icon: 'touch_app', title: 'Botones e íconos' },
    { id: 'busqueda', icon: 'search', title: 'Búsqueda' },
    { id: 'resultados', icon: 'view_agenda', title: 'Resultados de búsqueda' },
    { id: 'tabla', icon: 'table_chart', title: 'Tabla de registros' },
    { id: 'seleccion', icon: 'checklist', title: 'Selección y acciones masivas' },
    { id: 'graficos', icon: 'bar_chart', title: 'Gráficos y reportes' },
    { id: 'paginacion', icon: 'view_carousel', title: 'Paginación' },
    { id: 'formularios', icon: 'edit_note', title: 'Formularios' },
    { id: 'temas', icon: 'palette', title: 'Cambio de tema' },
    { id: 'seguridad', icon: 'shield', title: 'Seguridad' },
    { id: 'configuracion', icon: 'settings', title: 'Configuración de cuenta' },
    { id: 'historial', icon: 'history', title: 'Historial de actividad' }
  ];

  readonly botonesIconos: IconExplanation[] = [
    {
      icon: 'add',
      label: 'Agregar',
      description: 'Crea un nuevo registro dentro del módulo en el que te encuentres.'
    },
    {
      icon: 'edit',
      label: 'Editar',
      description: 'Abre un registro existente en modo edición para modificar su información.'
    },
    {
      icon: 'delete',
      label: 'Eliminar',
      description: 'Elimina un registro de forma permanente. Siempre pide confirmación antes de ejecutarse.'
    },
    {
      icon: 'visibility',
      label: 'Ver detalle',
      description: 'Muestra la información completa de un registro sin necesidad de editarlo.'
    },
    {
      icon: 'print',
      label: 'Imprimir',
      description: 'Genera una versión imprimible del contenido que estás visualizando.'
    },
    {
      icon: 'file_download',
      label: 'Exportar',
      description: 'Descarga la información visible en un archivo (por ejemplo PDF o Excel) para usarla fuera del sistema.'
    },
    {
      icon: 'check',
      label: 'Guardar',
      description: 'Confirma y guarda los cambios realizados en un formulario.'
    },
    {
      icon: 'close',
      label: 'Cancelar',
      description: 'Descarta los cambios sin guardarlos y cierra el formulario o panel activo.'
    },
    {
      icon: 'cleaning_services',
      label: 'Limpiar',
      description: 'Borra los valores ingresados en los campos de búsqueda o formularios para iniciar una nueva operación.'
    },
    {
      icon: 'check_circle',
      label: 'Activar',
      description: 'Cambia el estado de un registro a activo permitiendo nuevamente su uso dentro del sistema.'
    },
    {
      icon: 'block',
      label: 'Desactivar',
      description: 'Cambia el estado de un registro a inactivo evitando su uso sin eliminar la información.'
    },
    {
      icon: 'arrow_upward',
      label: 'Orden ascendente',
      description: 'Ordena la información de menor a mayor, por ejemplo de A a Z o del valor más bajo al más alto.'
    },
    {
      icon: 'arrow_downward',
      label: 'Orden descendente',
      description: 'Ordena la información de mayor a menor, por ejemplo de Z a A o del valor más alto al más bajo.'
    },
    {
      icon: 'message',
      label: 'Ver mensaje',
      description: 'Muestra el contenido de un mensaje, notificación o comunicación asociada a un registro.'
    },
    {
      icon: 'account_circle',
      label: 'Ver perfil',
      description: 'Permite consultar la información detallada del perfil de un usuario o persona registrada.'
    },
    {
      icon: 'picture_as_pdf',
      label: 'Generar PDF',
      description: 'Genera un documento en formato PDF con la información seleccionada para visualizar o compartir.'
    },
    {
      icon: 'description',
      label: 'Generar Excel',
      description: 'Exporta la información del sistema a un archivo Excel para análisis o gestión externa.'
    },
    {
      icon: 'insert_drive_file',
      label: 'Ver archivo',
      description: 'Permite abrir o consultar un archivo asociado a un registro dentro del sistema.'
    },
    {
      icon: 'bar_chart',
      label: 'Ver gráficos',
      description: 'Muestra representaciones gráficas de la información para facilitar el análisis y la toma de decisiones.'
    },
    {
      icon: 'upload_file',
      label: 'Importar archivo',
      description: 'Carga información desde un archivo externo para registrarla dentro del sistema.'
    },
  ];

  readonly formularioIconos: IconExplanation[] = [
    {
      icon: 'mail',
      label: 'Correo electrónico',
      description: 'Marca los campos donde debes ingresar un correo electrónico válido.'
    },
    {
      icon: 'lock',
      label: 'Contraseña',
      description: 'Marca los campos de contraseña. Permite ingresar información protegida.'
    },
    {
      icon: 'call',
      label: 'Teléfono',
      description: 'Marca los campos donde se registra un número de contacto.'
    },
    {
      icon: 'calendar_today',
      label: 'Fecha',
      description: 'Marca los campos que requieren seleccionar una fecha desde un calendario.'
    },
    {
      icon: 'schedule',
      label: 'Hora',
      description: 'Marca los campos donde se debe seleccionar o ingresar una hora específica.'
    },
    {
      icon: 'location_on',
      label: 'Ubicación',
      description: 'Marca campos relacionados con dirección, ubicación o lugar de registro.'
    },
    {
      icon: 'attach_file',
      label: 'Archivo',
      description: 'Marca los campos donde se permite adjuntar documentos o archivos al registro.'
    },
    {
      icon: 'arrow_drop_down_circle',
      label: 'Lista desplegable',
      description: 'Marca campos de selección donde el usuario debe elegir una opción disponible.'
    },
    {
      icon: 'check_box',
      label: 'Checkbox',
      description: 'Permite seleccionar una o varias opciones dentro de un conjunto de valores.'
    },
    {
      icon: 'radio_button_checked',
      label: 'Radio button',
      description: 'Permite seleccionar únicamente una opción entre varias alternativas disponibles.'
    },
    {
      icon: 'text_fields',
      label: 'Campo de texto',
      description: 'Marca campos donde el usuario puede ingresar información escrita.'
    },
    {
      icon: 'numbers',
      label: 'Número',
      description: 'Marca campos donde se deben ingresar valores numéricos.'
    }
  ];
  readonly seleccionIconos: IconExplanation[] = [
    { icon: 'check_box', label: 'Casilla de selección', description: 'Marca uno o varios registros de la tabla para aplicarles una acción en conjunto.' },
    { icon: 'check_box_outline_blank', label: 'Seleccionar todo', description: 'Ubicada en el encabezado de la tabla; marca o desmarca todos los registros visibles a la vez.' },
    { icon: 'toggle_on', label: 'Activar', description: 'Cambia el estado de los registros seleccionados a "activo".' },
    { icon: 'toggle_off', label: 'Desactivar', description: 'Cambia el estado de los registros seleccionados a "inactivo".' },
    { icon: 'delete_sweep', label: 'Eliminar seleccionados', description: 'Elimina de forma permanente todos los registros marcados. Pide confirmación antes de ejecutarse.' },
  ];

  readonly graficosIconos: IconExplanation[] = [
    { icon: 'bar_chart', label: 'Gráfico de barras', description: 'Compara valores entre distintas categorías dentro de un mismo periodo.' },
    { icon: 'show_chart', label: 'Gráfico de líneas', description: 'Muestra la evolución de un dato a lo largo del tiempo.' },
    { icon: 'pie_chart', label: 'Gráfico circular', description: 'Muestra la proporción de cada categoría respecto al total.' },
    { icon: 'file_download', label: 'Exportar reporte', description: 'Descarga el gráfico o los datos que lo componen en un archivo (PDF o Excel) para usarlos fuera del sistema.' },
    { icon: 'date_range', label: 'Rango de fechas', description: 'Define el periodo de tiempo que se quiere analizar en el reporte.' },
  ];

  readonly seguridadIconos: IconExplanation[] = [
    { icon: 'shield', label: 'Seguridad', description: 'Sección donde se gestionan las opciones de protección de tu cuenta.' },
    { icon: 'verified_user', label: 'Verificación en dos pasos', description: 'Agrega un paso adicional de confirmación al iniciar sesión, además de la contraseña.' },
    { icon: 'devices', label: 'Sesiones activas', description: 'Muestra los dispositivos donde tu cuenta tiene una sesión iniciada actualmente.' },
    { icon: 'history_toggle_off', label: 'Cerrar sesiones', description: 'Permite cerrar el acceso desde dispositivos que ya no reconoces o no usas.' },
  ];

  readonly configuracionIconos: IconExplanation[] = [
    {
      icon: 'lock_reset',
      label: 'Cambiar contraseña',
      description: 'Permite actualizar la contraseña de tu cuenta ingresando la actual y la nueva.'
    },
    {
      icon: 'account_circle',
      label: 'Datos del usuario',
      description: 'Muestra y permite editar tu información personal registrada en el sistema.'
    },
    {
      icon: 'person',
      label: 'Mi perfil',
      description: 'Permite consultar y administrar la información personal, preferencias y datos de tu cuenta.'
    },
    {
      icon: 'palette',
      label: 'Cambio de tema',
      description: 'Permite cambiar la apariencia del sistema seleccionando entre diferentes modos visuales.'
    }
  ];

  scrollTo(id: string): void {
    this.activeSection.set(id);
    const el = document.getElementById(id);
    el?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

}
