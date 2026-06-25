import { CommonModule } from '@angular/common';
import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-principal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './principal.html',
  styleUrl: './principal.css',
})
export class Principal {

constructor(private router: Router) {}

  isScrolled = false;

  @HostListener('window:scroll')
  onScroll(): void {
    this.isScrolled = window.scrollY > 40;
  }

  onLogin(): void {
    this.router.navigate(['/auth/login']);

  }

  scrollTo(sectionId: string): void {
    const el = document.getElementById(sectionId);
    if (el) el.scrollIntoView({ behavior: 'smooth' });
  }

  navTo(sectionId: string): void {
    const navMenu = document.getElementById('navMenu');
    if (navMenu && navMenu.classList.contains('show')) {
      const bsCollapse = (window as any).bootstrap?.Collapse.getInstance(navMenu);
      if (bsCollapse) {
        bsCollapse.hide();
      } else {
        navMenu.classList.remove('show');
      }
      setTimeout(() => this.scrollTo(sectionId), 350);
    } else {
      this.scrollTo(sectionId);
    }
  }

  niveles = [
    {
      icon: 'fas fa-pencil-ruler',
      nombre: 'Primaria',
      grados: '1.° — 6.° Grado',
      edad: '6 a 12 años',
      colorClass: 'nivel-primaria',
      foto: 'https://images.unsplash.com/photo-1503676260728-1c00da094a0b?w=800&q=80',
      items: [
        'Comunicación y Lenguaje',
        'Matemáticas',
        'Ciencias Naturales',
        'Personal Social',
        'Arte y Cultura',
        'Inglés',
        'Educación Física'
      ]
    },
    {
      icon: 'fas fa-book-open',
      nombre: 'Secundaria',
      grados: '1.° — 5.° Año',
      edad: '12 a 17 años',
      colorClass: 'nivel-secundaria',
      foto: 'https://images.unsplash.com/photo-1509062522246-3755977927d7?w=800&q=80',
      items: [
        'Comunicación',
        'Matemáticas',
        'Física y Química',
        'Biología',
        'Historia y Geografía',
        'Inglés Avanzado',
        'Robótica y Tecnología',
        'Preuniversitario (4.° y 5.°)'
      ]
    }
  ];

  // Actividades y programas complementarios
  programas = [
    {
      icon: 'fas fa-laptop-code',
      titulo: 'Robótica y Programación',
      nivel: 'Primaria y Secundaria',
      descripcion: 'Talleres de robótica, pensamiento computacional y programación integrados desde primaria, con proyectos avanzados en secundaria.'
    },
    {
      icon: 'fas fa-language',
      titulo: 'Inglés Intensivo',
      nivel: 'Primaria y Secundaria',
      descripcion: 'Clases de inglés desde 1.° de primaria con docentes certificados. En secundaria se alcanza nivel B2 al egresar.'
    },
    {
      icon: 'fas fa-running',
      titulo: 'Deporte y Vida Sana',
      nivel: 'Primaria y Secundaria',
      descripcion: 'Fútbol, vóley, atletismo y natación. Participamos en campeonatos interescolares en ambos niveles.'
    },
    {
      icon: 'fas fa-paint-brush',
      titulo: 'Arte y Expresión',
      nivel: 'Primaria y Secundaria',
      descripcion: 'Talleres de dibujo, música, danza y teatro que complementan la formación académica y desarrollan talentos únicos.'
    },
    {
      icon: 'fas fa-flask',
      titulo: 'Laboratorio de Ciencias',
      nivel: 'Primaria y Secundaria',
      descripcion: 'Laboratorios equipados de física, química y biología. Experimentos reales desde primaria para despertar la vocación científica.'
    },
    {
      icon: 'fas fa-graduation-cap',
      titulo: 'Preuniversitario',
      nivel: 'Secundaria — 4.° y 5.° año',
      descripcion: 'Preparación intensiva para el ingreso universitario con simulacros, asesoría vocacional y reforzamiento en todas las áreas.'
    }
  ];

  features = [
    { icon: 'fas fa-chalkboard-teacher', titulo: 'Docentes Especializados', descripcion: 'Maestros con formación continua en pedagogía de primaria y secundaria.' },
    { icon: 'fas fa-microscope', titulo: 'Laboratorios Modernos', descripcion: 'Laboratorios de ciencias y tecnología para ambos niveles educativos.' },
    { icon: 'fas fa-shield-alt', titulo: 'Seguridad Total', descripcion: 'Cámaras, portería controlada y protocolo de emergencias en todo el colegio.' },
    { icon: 'fas fa-users', titulo: 'Comunidad Unida', descripcion: 'Padres, maestros y alumnos de primaria y secundaria comprometidos con la excelencia.' }
  ];

  testimonios = [
    {
      icon: 'fas fa-user',
      texto: 'Mi hija lleva cuatro años en primaria y el avance es notable. Los maestros son muy dedicados y el ambiente es excelente. ¡Totalmente recomendado!',
      nombre: 'María García',
      cargo: 'Madre de alumna, 4.° Primaria',
      estrellas: [1, 2, 3, 4, 5]
    },
    {
      icon: 'fas fa-user-graduate',
      texto: 'Gracias a la preparación en secundaria, ingresé a la universidad con una base sólida. El programa preuniversitario de 5.° año marcó la diferencia.',
      nombre: 'Carlos Mendoza',
      cargo: 'Egresado 5.° Secundaria, 2024',
      estrellas: [1, 2, 3, 4, 5]
    },
    {
      icon: 'fas fa-user-tie',
      texto: 'Mi hijo está en 3.° de secundaria y lo que más valoro es la formación integral. No solo aprende contenidos; aprende a ser una mejor persona.',
      nombre: 'Roberto Silva',
      cargo: 'Padre de alumno, 3.° Secundaria',
      estrellas: [1, 2, 3, 4, 5]
    }
  ];

  valores = [
    { icon: 'fas fa-heart', titulo: 'Valores y Ética', descripcion: 'En primaria y secundaria formamos personas íntegras, responsables y respetuosas.' },
    { icon: 'fas fa-flask', titulo: 'Ciencia e Innovación', descripcion: 'Proyectos STEM en ambos niveles que despiertan la curiosidad y el pensamiento científico.' },
    { icon: 'fas fa-globe', titulo: 'Visión de Futuro', descripcion: 'Preparamos a nuestros alumnos de secundaria para los retos de la vida universitaria y profesional.' }
  ];
}