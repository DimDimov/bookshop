INSERT IGNORE INTO author (id, biography, name, photo_url)
VALUES (
1,
'Geburts- und Wohnhaus in Jasnaja Poljana
Speisezimmer des Wohnhauses (heute Museum) in Jasnaja Poljana.
Lew Tolstoi entstammte dem russischen Adelsgeschlecht der Tolstois.'
'Er war das vierte von fünf Kindern. Sein Vater war der russische Graf Nikolai Iljitsch Tolstoi (1794–1837), seine Mutter Marija Nikolajewna, geb. Fürstin Wolkonskaja (1790–1830).'
'[1] Als er mit neun Jahren Vollwaise wurde, übernahm die Schwester seines Vaters die Vormundschaft.'
'An der Universität Kasan begann er 1844 ein Studium der orientalischen Sprachen. Nach einem Wechsel an die juristische Fakultät brach er das Studium 1847 ab,'
'um die Lage der 350 geerbten Leibeigenen im Stammgut der Familie in Jasnaja Poljana mit Landreformen zu verbessern (Der Morgen eines Gutsbesitzers).'
'Nach anderen Quellen bestand er 1848 noch das juristische Kandidatenexamen an der Petersburger Universität „mit knapper Not“ und kehrte dann in sein Dorf zurück.[2]',
'Lew Tolstoi',
'/uploads/authors/1763544717496_Lew Tolstoi.jpeg'
);

INSERT IGNORE INTO book (id, genre, image_book, isbn, price, slug, title, toggled_book, author_id)
VALUES (
1,
'Klassik',
'/uploads/books/Krieg und Frieden.jpeg',
23567,
25.5,
'krieg_und_frieden',
'Krieg und Frieden',
true,
1
);

insert IGNORE into book_description(id, description, book_id)
VALUES (
1,
'Krieg und Frieden (russisch Война и миръ (Originalschreibweise), deutsche Transkription Woina i mir, Transliteration Vojna i mir,'
'Aussprache [vʌj.''na ɪ.''mir]) ist ein im realistischen Stil geschriebener,'
'1868/1869 publizierter historischer Roman des russischen Schriftstellers Lew Tolstoi.
Krieg und Frieden gilt als eines der bedeutendsten Werke der Weltliteratur (s. Rezeption und Adaptionen).'
'In seiner Mischung aus historischem Roman und militär-politischen Darstellungen sowie Analysen der'
'zaristischen Feudalgesellschaft während der napoleonischen Ära Anfang des 19. Jahrhunderts in Russland und den Kriegen zwischen 1805 und '
'1812 mit der Invasion Russlands 1812 nimmt Tolstois Werk die Montagetechnik moderner Romane des 20. Jahrhunderts vorweg.',
1
);