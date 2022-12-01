package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.frends.impl.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.like.impl.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource("/application-test.properties")
@Sql(value = {"test-schema.sql", "test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmoRateApplicationTests {

    @Autowired
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private final UserDbStorage userStorage;
    @Autowired
    private final FriendDbStorage friendStorage;
    @Autowired
    private final FilmDbStorage filmDbStorage;
    @Autowired
    private final MpaDbStorage mpaDbStorage;
    @Autowired
    private final GenreDbStorage genreDbStorage;
    @Autowired
    private final LikeDbStorage likeDbStorage;


    @Test
    @Sql(value = {"create-Users-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testFindUserById() {
        User requestUser = userStorage.getUserById(1);
        Optional<User> userOptional = Optional.ofNullable(requestUser);

        User userInsideDb = getUserById(1);
        assertThat(requestUser).isNotNull();
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", userInsideDb.getId()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", userInsideDb.getName()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("login", userInsideDb.getLogin()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("email", userInsideDb.getEmail()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("birthday", userInsideDb.getBirthday()));
    }

    @Test
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAddUser() {
        User requestUser = userStorage.addUser(new User(null, "test4Name", "test4Login", "test4@email.ru", LocalDate.of(2007, 3, 4)));
        Optional<User> userOptional = Optional.of(requestUser);

        User userInsideDb = getUserById(1);
        assertThat(userInsideDb).isNotNull();
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", userInsideDb.getId()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", userInsideDb.getName()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("login", userInsideDb.getLogin()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("email", userInsideDb.getEmail()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("birthday", userInsideDb.getBirthday()));
    }

    @Test
    @Sql(value = {"create-Users-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateUser() {
        User requestUser = userStorage.updateUser(new User(3, "test4Name", "test4Login", "test4@email.ru", LocalDate.of(2007, 3, 4)));
        Optional<User> userOptional = Optional.of(requestUser);

        User userInsideDb = getUserById(3);
        assertThat(userInsideDb).isNotNull();
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", userInsideDb.getId()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", userInsideDb.getName()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("login", userInsideDb.getLogin()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("email", userInsideDb.getEmail()))
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("birthday", userInsideDb.getBirthday()));
    }

    @Test
    @Sql(value = {"create-Users-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetUsers() {
        List<User> allUsersRequest = userStorage.getUsers();
        String sql = "select * from USERS";
        List<User> allUsersInsideDb = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
        assertThat(allUsersRequest)
                .isNotEmpty()
                .hasSize(3)
                .doesNotHaveDuplicates()
                .contains(allUsersInsideDb.get(0), allUsersInsideDb.get(1), allUsersInsideDb.get(2))
                .endsWith(allUsersInsideDb.get(2))
                .startsWith(allUsersInsideDb.get(0));
    }

    @Test
    @Sql(value = {"create-Users-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAddFriends() {
        friendStorage.addFriend(2, 1);
        User userInsideDb = getUserById(2);
        assertThat(userInsideDb.getFriends()).isNotNull().contains(1);
    }

    @Test
    @Sql(value = {"create-Users-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteAddFriends() {
        friendStorage.deleteFriend(1, 2);
        User userInsideDb = getUserById(2);
        assertThat(userInsideDb.getFriends()).isNotNull().isEmpty();
    }

    @Test
    @Sql(value = {"create-Users-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllFriendByUser() {
        List<Integer> allFriends = friendStorage.getAllFriendByUser(1);

        assertThat(allFriends)
                .isNotEmpty()
                .hasSize(1)
                .contains(2);
    }

    @Test
    @Sql(value = {"create-Films-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testFindFilmById() {
        Film requestFilm = filmDbStorage.getFilmById(1);
        Optional<Film> filmOptional = Optional.ofNullable(requestFilm);

        Film filmInsideDb = getFilmById(1);
        assertThat(requestFilm).isNotNull();
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", filmInsideDb.getId()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("name", filmInsideDb.getName()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("description", filmInsideDb.getDescription()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("releaseDate", filmInsideDb.getReleaseDate()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("duration", filmInsideDb.getDuration()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("mpa", filmInsideDb.getMpa()));
    }

    @Test
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAddFilm() {
        Film requestFilm = new Film(null, "testName", "testDescription", LocalDate.of(2007, 3, 4), 100, new MPA(1, "G"));
        Optional<Film> filmOptional = Optional.ofNullable(filmDbStorage.addFilm(requestFilm));
        Film filmInsideDb = filmDbStorage.getFilmById(1);
        assertThat(filmInsideDb).isNotNull();
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", filmInsideDb.getId()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("name", filmInsideDb.getName()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("description", filmInsideDb.getDescription()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("releaseDate", filmInsideDb.getReleaseDate()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("duration", filmInsideDb.getDuration()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("mpa", filmInsideDb.getMpa()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("genres", filmInsideDb.getGenres()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("likes", filmInsideDb.getLikes()));
    }

    @Test
    @Sql(value = {"create-Films-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateFilm() {
        Film requestFilm = new Film(1, "test4Name", "test4Description", LocalDate.of(2007, 3, 4), 400, new MPA(4, "R"));
        Optional<Film> filmOptional = Optional.of(filmDbStorage.updateFilms(requestFilm));

        Film filmInsideDb = getFilmById(1);
        assertThat(filmInsideDb).isNotNull();
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", filmInsideDb.getId()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("name", filmInsideDb.getName()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("description", filmInsideDb.getDescription()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("releaseDate", filmInsideDb.getReleaseDate()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("duration", filmInsideDb.getDuration()))
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("mpa", filmInsideDb.getMpa()));
    }

    @Test
    @Sql(value = {"create-Films-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetFilms() {
        List<Film> allFilmsRequest = filmDbStorage.getFilms();
        String sql = "select * from FILMORATETEST.PUBLIC.FILM";
        List<Film> allFilmsInsideDb = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        assertThat(allFilmsRequest)
                .isNotEmpty()
                .hasSize(3)
                .doesNotHaveDuplicates()
                .contains(allFilmsInsideDb.get(0), allFilmsInsideDb.get(1), allFilmsInsideDb.get(2))
                .endsWith(allFilmsInsideDb.get(2))
                .startsWith(allFilmsInsideDb.get(0));
    }

    @Test
    @Sql(value = {"create-Films-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetMpaById() {
        MPA mpaRating = mpaDbStorage.getById(1);
        Optional<MPA> optionalMPA = Optional.of(mpaRating);
        assertThat(optionalMPA)
                .isPresent()
                .hasValueSatisfying(mpa -> assertThat(mpa).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(mpa -> assertThat(mpa).hasFieldOrPropertyWithValue("name", "G"));
    }

    @Test
    public void testGetAllMpa() {
        List<MPA> allMpa = mpaDbStorage.getAll();
        assertThat(allMpa)
                .isNotEmpty()
                .hasSize(5)
                .doesNotHaveDuplicates()
                .contains(new MPA(1, "G"),
                        new MPA(2, "PG"),
                        new MPA(3, "PG-13"),
                        new MPA(4, "R"),
                        new MPA(5, "NC-17"))
                .endsWith(new MPA(5, "NC-17"))
                .startsWith(new MPA(1, "G"));
    }

    @Test
    public void testGetAllGenre() {
        List<Genre> allGenre = genreDbStorage.getAll();
        assertThat(allGenre)
                .isNotEmpty()
                .hasSize(6)
                .doesNotHaveDuplicates()
                .contains(new Genre(1, "Комедия"),
                        new Genre(2, "Драма"),
                        new Genre(3, "Мультфильм"),
                        new Genre(4, "Триллер"),
                        new Genre(5, "Документальный"),
                        new Genre(6, "Боевик"))
                .endsWith(new Genre(6, "Боевик"))
                .startsWith(new Genre(1, "Комедия"));
    }

    @Test
    @Sql(value = {"create-Films-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetGenreById() {
        Genre genreById = genreDbStorage.getById(1);
        Optional<Genre> optionalGenre = Optional.of(genreById);
        assertThat(optionalGenre)
                .isPresent()
                .hasValueSatisfying(genre -> assertThat(genre).hasFieldOrPropertyWithValue("id", 1))
                .hasValueSatisfying(genre -> assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия"));
    }

    @Test
    @Sql(value = {"create-Films-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetGenresByFilmId() {
        List<Genre> genres = genreDbStorage.getByFilmId(3);
        assertThat(genres)
                .isNotEmpty()
                .hasSize(1)
                .doesNotHaveDuplicates()
                .contains(new Genre(1, "Комедия"));
    }

    @Test
    @Sql(value = {"create-Films-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAssignGenre() {
        genreDbStorage.assignGenre(1, 2);
        Film film = getFilmById(1);
        film.setGenres(genreDbStorage.getByFilmId(1));
        List<Genre> genres = film.getGenres();
        assertThat(genres)
                .isNotEmpty()
                .hasSize(2)
                .doesNotHaveDuplicates()
                .contains(new Genre(3, "Мультфильм"),
                        new Genre(2, "Драма"))
                .startsWith(new Genre(3, "Мультфильм"))
                .endsWith(new Genre(2, "Драма"));
    }

    @Test
    @Sql(value = {"create-Films-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteGenre() {
        genreDbStorage.delete(2);
        Film film = getFilmById(2);
        film.setGenres(genreDbStorage.getByFilmId(film.getId()));
        List<Genre> genres = film.getGenres();
        assertThat(genres)
                .isEmpty();
    }

    @Test
    @Sql(value = {"create-Films-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAddLike() {
        likeDbStorage.addLike(2, 3);
        Film film = getFilmById(3);
        film.setLikes(likeDbStorage.getFilmLikeId(film.getId()));
        List<Integer> likes = film.getLikes();
        assertThat(likes)
                .isNotEmpty()
                .hasSize(2)
                .doesNotHaveDuplicates()
                .contains(1, 2);
    }

    @Test
    @Sql(value = {"create-Films-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testDeleteLike() {
        likeDbStorage.deleteLike(2, 1);
        Film film = getFilmById(1);
        film.setLikes(likeDbStorage.getFilmLikeId(film.getId()));
        List<Integer> likes = film.getLikes();
        assertThat(likes)
                .isNotEmpty()
                .hasSize(1)
                .doesNotHaveDuplicates()
                .contains(1);
    }

    @Test
    @Sql(value = {"create-Films-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"clear-data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetFilmLikesId() {
        List<Integer> likes = likeDbStorage.getFilmLikeId(1);
        assertThat(likes)
                .isNotEmpty()
                .hasSize(2)
                .doesNotHaveDuplicates()
                .contains(2, 1);
    }


    private Film getFilmById(Integer id) {
        Film film = new Film();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILMORATETEST.PUBLIC.FILM where ID = ?", id);
        if (filmRows.next()) {
            film = Film.builder()
                    .id(filmRows.getInt("id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .mpa(mpaDbStorage.getById(filmRows.getInt("mpa_id")))
                    .build();
        }
        return film;
    }

    private User getUserById(Integer id) {
        User user = new User();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from FILMORATETEST.PUBLIC.USERS where ID = ?", id);
        if (userRows.next()) {
            user = User.builder()
                    .id(userRows.getInt("id"))
                    .name(userRows.getString("name"))
                    .login(userRows.getString("login"))
                    .email(userRows.getString("email"))
                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
                    .friends(friendStorage.getAllFriendByUser(id))
                    .build();
        }
        return user;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpaDbStorage.getById(rs.getInt("mpa_id")))
                .build();
        return film;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}