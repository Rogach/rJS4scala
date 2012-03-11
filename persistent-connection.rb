#!/usr/bin/ruby

require 'socket'

while true
  begin
    s = TCPSocket.open('localhost', ARGV[0])
    if ARGV[1] != nil
      s.puts ARGV[1]
    end
    th = Thread.start do ||
      while iline = $stdin.gets.chomp
        s.puts iline
      end
    end
    while line = s.gets
      puts line
    end
    th.kill
    s.close
    puts "Converstaion ended"
  rescue
  end
  sleep 0.1
end
