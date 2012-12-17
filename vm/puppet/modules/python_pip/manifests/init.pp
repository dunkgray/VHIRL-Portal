# Installs python AND pip (python install program) for Centos
# Depends on the stahnma/epel module
class python_pip {

    # Install Python
    package { ["python", "python-devel"]: 
        ensure => installed,
    }

    # Python pip comes from EPEL
    import "epel"
    class {"epel":}
    
    # Install Python-Pip
    package { ["python-pip"]: 
        ensure => installed,
    }

    # Centos packages call the 'pip' executable 'pip-python' which can cause problems.
    # TODO: don't use hardcoded paths here
    case $operatingsystem {
        centos, redhat: { 
            file { '/usr/bin/pip':
                ensure => 'link',
                target => '/usr/bin/pip-python',
            }
        }
    }
}