define trac::site(
	$envid = "$title",
	$version,
	$xmlrpcplugin = "trunk",
	$accountmanagerplugin = "",
	$allbasicauth = false,
	$certauth = false,
	$digestauth = false,
	$base = "/home/tools/trac",
) { 
	$prefix = "$base/share/trac-$version"
	$envbase = "$base/var/$envid"
	$env = "$base/var/$envid/env"
	$conf = "$base/conf.d"

	file { "$envbase":
		ensure => "directory",
		owner => "www-data",
	}

	file { "$envbase/svn":
		ensure => "directory",
        require => File["$envbase"],
		owner => "www-data",
	}

	exec { "svn create $envid":
        command => "svnadmin create $envbase/svn",
        require => File["$envbase/svn"],
		creates => "$envbase/svn/format",
	}
	
	exec { "initenv $envid":
        command => "$base/bin/tracadmin-$version $env initenv $envid sqlite:db/trac.db svn $envbase/svn",
		creates => "$env",
		user => "www-data",
		require => Exec["svn create $envid"],
	}

	exec { "add admin permissions $envid":
        command => "$base/bin/tracadmin-$version $env permission add admin@mylyn.eclipse.org TRAC_ADMIN",
		user => "www-data",
		environment => "PYTHON_EGG_CACHE=/tmp/eggs",
		require => Exec["initenv $envid"],
		onlyif => "$base/bin/tracadmin-$version $env permission list admin@mylyn.eclipse.org | (grep -qE 'admin.*TRAC_ADMIN'; test $? != 0)"
	}

	exec { "add tests permissions $envid":
        command => "$base/bin/tracadmin-$version $env permission add tests@mylyn.eclipse.org TICKET_ADMIN TICKET_CREATE TICKET_MODIFY",
		user => "www-data",
		environment => "PYTHON_EGG_CACHE=/tmp/eggs",
		require => Exec["initenv $envid"],
		onlyif => "$base/bin/tracadmin-$version $env permission list tests@mylyn.eclipse.org | (grep -qE 'tests.*TICKET_ADMIN'; test $? != 0)"
	}

	exec { "add user permissions $envid":
        command => "$base/bin/tracadmin-$version $env permission add user@mylyn.eclipse.org TICKET_CREATE TICKET_MODIFY",
		user => "www-data",
		environment => "PYTHON_EGG_CACHE=/tmp/eggs",
		require => Exec["initenv $envid"],
		onlyif => "$base/bin/tracadmin-$version $env permission list user@mylyn.eclipse.org | (grep -qE 'user.*TICKET_CREATE'; test $? != 0)"
	}
	
	file { "$env/conf/trac.ini":
    	content => template('trac/trac.ini.erb'),
    	require => Exec["initenv $envid"],
	}

	file { "$conf/$envid.conf":
    	content => template('trac/trac.conf.erb'),
	}

	if $digestauth {
		file { "$envbase/htpasswd.digest":
    		content => template('trac/htpasswd.digest.erb'),
			require => File["$envbase"],
		}
	} else {
		file { "$envbase/htpasswd":
	    	content => template('trac/htpasswd.erb'),
			require => File["$envbase"],
		}
	}

	file { "$envbase/trac-$version.fcgi":
    	content => template('trac/trac.fcgi.erb'),
		mode => 755,
		require => File["$envbase"],
		notify  => Service["apache2"],
	}

	if $xmlrpcplugin {
		file { "$env/plugins/TracXMLRPC.egg":
			source => "$base/src/xmlrpcplugin-$xmlrpcplugin/src/dist/TracXMLRPC.egg",
			require => Exec["initenv $envid"],
		}

		exec { "add xmlrpc permissions $envid":
	        command => "$base/bin/tracadmin-$version $env permission add tests@mylyn.eclipse.org XML_RPC",
			user => "www-data",
			environment => "PYTHON_EGG_CACHE=/tmp/eggs",
			require => File["$env/plugins/TracXMLRPC.egg"],
			onlyif => "$base/bin/tracadmin-$version $env permission list tests@mylyn.eclipse.org | (grep -qE 'tests.*XML_RPC'; test $? != 0)"
		}
	} 

	if $accountmanagerplugin {
		file { "$env/plugins/TracAccountManager.egg":
			source => "$base/src/accountmanagerplugin-$accountmanagerplugin/src/dist/TracAccountManager.egg",
			require => Exec["initenv $envid"],
		}
	} 

}
